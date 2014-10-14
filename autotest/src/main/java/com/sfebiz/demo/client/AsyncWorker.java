package com.sfebiz.demo.client;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncWorker {
    public static final String TAG = "com.sfebiz.android.util.AsyncWorker";
    int threadCount   = 0;
    int workQueueSize = 0;

    private ThreadPoolExecutor executor = null;
    private Handler uiCallbackHandler;

    private static class WebRequestThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        WebRequestThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + poolNumber.getAndIncrement() + "-" + name + "-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);

            t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    Log.e(TAG, thread.getName() + " thrown an exception : ", ex);
                }
            });
            return t;
        }
    }

    public static AsyncWorker build(int core, int queueSize, String poolName) {
        BlockingQueue<Runnable> queue = null;
        if (queueSize > 127) {
            queue = new LinkedBlockingQueue<Runnable>(127);
        } else if (queueSize > 0) {
            queue = new ArrayBlockingQueue<Runnable>(queueSize);
        } else {
            queue = new SynchronousQueue<Runnable>();
        }
        AsyncWorker worker = new AsyncWorker();

        RejectedExecutionHandler rejectedHanlder = null;
        if (ApiConfig.isDebug) {
            rejectedHanlder = new RejectedExecutionHandler() {

                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    Log.w(TAG, "A task is rejected. " + r.toString());
                }
            };
        } else {
            rejectedHanlder = new ThreadPoolExecutor.DiscardPolicy();
        }
        worker.executor = new ThreadPoolExecutor(core, core, 30, TimeUnit.SECONDS, queue, new WebRequestThreadFactory(poolName), rejectedHanlder);

        worker.uiCallbackHandler = new Handler(Looper.getMainLooper());
        worker.threadCount = core;
        worker.workQueueSize = queueSize;

        return worker;
    }

    private AsyncWorker() {}

    /**
     * 异步执行业务，完成后将callback发送到主线程执行, callback中需要判断相关资源(如UI资源)是否处于有效状态
     *
     * @param request  要执行的Api请求
     * @param callback 要在请求结束后在主线程执行的回调
     */
    public <T> void startAsyncWork(final Productor<T> request, final Consumer<T> callback) {
        startAsyncWork(request, callback, null);
    }

    /**
     * 异步执行业务，完成后将callback发送到主线程执行, callback中需要判断相关资源(如UI资源)是否处于有效状态。 如果有异常情况则通过errorCallback回报
     *
     * @param request     要执行的Api请求
     * @param callback    要在请求结束后在主线程执行的回调
     * @param errCallback 执行异步任务出错时执行的回调
     */
    public <T> void startAsyncWork(final Productor<T> request, final Consumer<T> callback, final ErrorCallback errCallback) {
        startAsyncWork(request, uiCallbackHandler, callback, errCallback);
    }

    /**
     * 异步执行业务，完成后将callback发送到callbackHandler的宿主线程执行, callback中需要判断相关资源(如UI资源)是否处于有效状态。 如果有异常情况则通过errorCallback回报
     *
     * @param request         要执行的Api请求
     * @param callbackHandler callback执行的宿主线程
     * @param callback        要在请求结束后在主线程执行的回调
     * @param errCallback     执行异步任务出错时执行的回调
     */
    public <T> void startAsyncWork(final Productor<T> request, final Handler callbackHandler, final Consumer<T> callback,
                                   final ErrorCallback errCallback) {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                T response = null;
                if (request != null) {
                    try {
                        response = request.run();
                    } catch (final LocalException e) {
                        if (errCallback != null) {
                            callbackHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    errCallback.callback(0, e.getCode(), e.getErrorData());
                                }
                            });
                            return;
                        } else {
                            Log.e(TAG, "local exception with code:" + e.getCode());
                        }
                    } catch (final Exception e) {
                        if (errCallback != null) {
                            callbackHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    errCallback.callback(0, LocalException.UNKNOWN, null);
                                }
                            });
                            return;
                        } else {
                            Log.e(TAG, "unknown exception :" + e);
                        }
                    }
                }

                final T resp = response;
                if (callback != null) {
                    callbackHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            callback.run(resp);
                        }
                    });
                }
            }

            @Override
            public String toString() {
                return request.toString();
            }
        });
    }
}

package com.sfebiz.demo.client.logger;

import com.sfebiz.demo.client.util.CompileConfig;

//本类是为了支持接口调用程序可以同时在android环境和PC环境下运行
public class LoggerFactory {
    public static Logger getLogger(String name) {
        //return new AndroidLogger(name);
        return new Log4JLogger(name);
    }

    public static Logger getLogger(Class<?> clz) {
        //return new AndroidLogger(clz);
        return new Log4JLogger(clz);
    }

    private static class Log4JLogger implements com.sfebiz.demo.client.logger.Logger {

        private org.apache.logging.log4j.Logger logger;

        Log4JLogger(Class<?> clazz) {
            logger = org.apache.logging.log4j.LogManager.getLogger(clazz);
        }

        Log4JLogger(String name) {
            logger = org.apache.logging.log4j.LogManager.getLogger(name);
        }

        public void debug(String str) {
            logger.debug(str);
        }

        public void info(String str) {
            logger.info(str);
        }

        public void warning(String str) {
            logger.warn(str);
        }

        public void error(String str) {
            logger.error(str);
        }

        public void error(Throwable e, String str) {
            logger.error(str, e);
        }

        public void fatal(String str) {
            logger.fatal(str);
        }
    }

    private static class AndroidLogger implements Logger {
        private String name;

        AndroidLogger(Class<?> clazz) {
            this.name = clazz.getName();
        }

        AndroidLogger(String name) {
            this.name = name;
        }

        public void debug(String str) {
            if (CompileConfig.isDebug) {
                android.util.Log.d(name, str);
            }
        }

        public void info(String str) {
            android.util.Log.i(name, str);
        }

        public void warning(String str) {
            android.util.Log.w(name, str);
        }

        public void error(Throwable e, String str) {
            android.util.Log.e(name, str, e);
        }

        public void error(String str) {
            android.util.Log.e(name, str);
        }

        public void fatal(String str) {
            android.util.Log.e(name, str);
        }
    }
}

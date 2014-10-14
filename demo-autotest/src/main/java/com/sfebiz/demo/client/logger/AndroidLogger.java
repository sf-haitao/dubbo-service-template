package com.sfebiz.demo.client.logger;

import android.util.Log;
import com.sfebiz.demo.client.util.CompileConfig;

/**
 * Android 的日志实现
 * 
 * @author rendong
 * 
 */
class AndroidLogger implements Logger {
    private String name;

    AndroidLogger(Class<?> clazz) {
        this.name = clazz.getName();
    }

    AndroidLogger(String name) {
        this.name = name;
    }

    /**
     * debug
     * 
     * @param str
     */
    public void debug(String str) {
        if (CompileConfig.isDebug) {
            Log.d(name, str);
        }
    }

    /**
     * info
     * 
     * @param str
     */
    public void info(String str) {
        Log.i(name, str);
    }

    /**
     * warning
     * 
     * @param str
     */
    public void warning(String str) {
        Log.w(name, str);
    }
    
    /**
     * error
     * 
     * @param str
     */
    public void error(Throwable e, String str) {
        Log.e(name, str, e);
    }

    /**
     * error
     * 
     * @param str
     */
    public void error(String str) {
        Log.e(name, str);
    }

    /**
     * fatal
     * 
     * @param str
     */
    public void fatal(String str) {
        Log.e(name, str);
    }
}

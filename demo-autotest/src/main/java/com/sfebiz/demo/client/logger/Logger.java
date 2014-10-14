package com.sfebiz.demo.client.logger;


/**
 * 日志类
 * @author rendong
 *
 */
public interface Logger {
    
    /**
     * debug
     * @param str
     */
    void debug(String str);
    
    /**
     * info
     * @param str
     */
    void info(String str);
    
    /**
     * warning
     * @param str
     */
    void warning(String str);
    
    /**
     * error
     * @param e
     * @param str
     */
    void error(Throwable e, String str);
    
    /**
     * error
     * @param str
     */
    void error(String str);
    
    /**
     * fatal
     * @param str
     */
    void fatal(String str);
}
package com.sfebiz.demo.client.logger;

import org.apache.logging.log4j.Logger;

class Log4JLogger implements com.sfebiz.demo.client.logger.Logger {

    private Logger logger;
        
    Log4JLogger(Class<?> clazz){
        logger = org.apache.logging.log4j.LogManager.getLogger(clazz);
    }
    
    Log4JLogger(String name){
        logger = org.apache.logging.log4j.LogManager.getLogger(name);
    }
        
    /**
     * debug
     * @param str
     */
    public void debug(String str) {
        logger.debug(str);
    }
    
    /**
     * info
     * @param str
     */
    public void info(String str){
        logger.info(str);
    }
    
    /**
     * warning
     * @param str
     */
    public void warning(String str){
        logger.warn(str);
    }
    
    /**
     * error
     * @param str
     */
    public void error(String str){
        logger.error(str);
    }
    
    /**
     * error
     * @param str
     */
    public void error(Throwable e, String str){
        logger.error(str, e);
    }
    
    /**
     * fatal
     * @param str
     */
    public void fatal(String str){
        logger.fatal(str);
    }
}

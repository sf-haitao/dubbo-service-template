package com.sfebiz.demo.client.logger;

//本类是为了支持接口调用程序可以同时在android环境和PC环境下运行
public class LoggerFactory {
    private static final int Android    = 1;
    private static final int Log4J      = 2;

    private static int       loggerType = Android;

    public static void setType(String type) {
        if ("Android".equalsIgnoreCase(type)) {
            loggerType = Android;
        } else if ("Log4J".equalsIgnoreCase(type)) {
            loggerType = Log4J;
        } else {
            throw new RuntimeException("unsupported logger type " + type);
        }
    }

    public static Logger getLogger(String name) {
        switch (loggerType) {
            case Android:
                return new AndroidLogger(name);
            case Log4J:
                return new Log4JLogger(name);
        }
        return null;
    }

    public static Logger getLogger(Class<?> clz) {
        switch (loggerType) {
            case Android:
                return new AndroidLogger(clz);
            case Log4J:
                return new Log4JLogger(clz);
        }
        return null;
    }
}

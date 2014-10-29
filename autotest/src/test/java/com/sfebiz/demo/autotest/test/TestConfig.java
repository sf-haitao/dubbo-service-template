package com.sfebiz.demo.autotest.test;

import java.util.Properties;

/**
 * Created by guankaiqiang521 on 2014/10/29.
 */
public class TestConfig {
    private static TestConfig instance = new TestConfig();
    private TestConfig() {
    }
    public static TestConfig getInstance() {
        return instance;
    }
    public static final void init(Properties properties) {
        synchronized (TestConfig.class) {
            if (instance == null) {
                instance = new TestConfig();
            }
            if (properties != null) {
                instance.setDebugDubboVersion(properties.getProperty("debug.dubbo.version"));
            }
        }
    }

    private String debugDubboVersion;

    public String getDebugDubboVersion() {
        return debugDubboVersion;
    }
    void setDebugDubboVersion(String debugDubboVersion) {
        if (debugDubboVersion != null && debugDubboVersion.length() != 0) {
            this.debugDubboVersion = debugDubboVersion;
            System.out.println("debug.dubbo.version:" + debugDubboVersion);
        }
    }
}

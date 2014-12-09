package com.sfebiz.demo.service.diamond;

import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;

import java.util.concurrent.Executor;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 14/11/26 下午11:56
 */
public class DynamicConfig {
    public static void main(String[] args) {
        ManagerListener testManagerListener = new TestDiamond();
        DefaultDiamondManager defaultDiamondManager = new DefaultDiamondManager("com.sfebiz.diamond.test", testManagerListener);
        String configInfo = defaultDiamondManager.getAvailableConfigureInfomation(1000);
        parseConfigInfo(configInfo);

    }
    static class TestDiamond implements ManagerListener {

        @Override
        public Executor getExecutor() {
            return null;
        }

        @Override
        public void receiveConfigInfo(String configInfo) {
            parseConfigInfo(configInfo);
        }
    }
    public static void parseConfigInfo(String configInfo) {

        if (null != configInfo && !(configInfo.trim()).equals("")) {
            System.out.println(configInfo);
        }
    }
}

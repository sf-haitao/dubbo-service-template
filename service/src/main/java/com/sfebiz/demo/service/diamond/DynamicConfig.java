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

        //实时监听配置的修改
        ManagerListener managerListener = new ManagerListener() {

            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {

                if (null != configInfo && !configInfo.equals("")) {
                    System.out.println("configInfo:" + configInfo);
                }
            }
        };

        DefaultDiamondManager defaultDiamondManager = new DefaultDiamondManager("DEFAULT_GROUP", "com.sfebiz.diamond.test", managerListener);

        System.out.println("configInfo: " + defaultDiamondManager.getAvailableConfigureInfomation(5000));


    }

}

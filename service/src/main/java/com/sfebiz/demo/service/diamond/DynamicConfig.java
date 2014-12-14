package com.sfebiz.demo.service.diamond;

import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 14/11/26 下午11:56
 */
public class DynamicConfig {

    private String xxxConfig;
    private Integer yyyConfig;

    private final static Logger logger = LoggerFactory.getLogger(DynamicConfig.class);

    private static final DynamicConfig dynamicConfig = new DynamicConfig();

    private ManagerListener dynamicConfigListener = new ManagerListener() {
        public void receiveConfigInfo(String configInfo) {
            configInfo = StringUtils.strip(configInfo);
            if (StringUtils.isNotBlank(configInfo)) {
                logger.warn("Change alipush-ps config: " + configInfo);
                Properties prop = new Properties();
                try {
                    prop.load(new StringReader(configInfo));
                    updateConfig(prop);
                } catch (Exception e) {
                    logger.error("Parse alipush-ps config failed:" + configInfo, e);
                }
            }
        }

        public Executor getExecutor() {
            return null;
        }
    };

    private void updateConfig(Properties properties) throws Exception {
        BeanUtils.copyProperties(this, properties);
    }

    public static DynamicConfig getInstance() {
        return dynamicConfig;
    }

    private DynamicConfig() {
        String groupId = "DEFAULT_GROUP";
        String dataId = "com.sfebiz.diamond.test";
        DefaultDiamondManager defaultDiamondManager = new DefaultDiamondManager(groupId, dataId, dynamicConfigListener);
        String configInfo = defaultDiamondManager.getAvailableConfigureInfomation(1000);
        dynamicConfigListener.receiveConfigInfo(configInfo);
    }

    public String getXxxConfig() {
        return xxxConfig;
    }

    public void setXxxConfig(String xxxConfig) {
        this.xxxConfig = xxxConfig;
    }

    public Integer getYyyConfig() {
        return yyyConfig;
    }

    public void setYyyConfig(Integer yyyConfig) {
        this.yyyConfig = yyyConfig;
    }

    public static void main(String[] args) {
        System.out.println("xxxConfig: " + DynamicConfig.getInstance().getXxxConfig());
        System.out.println("yyyConfig: " + DynamicConfig.getInstance().getYyyConfig());
    }

}

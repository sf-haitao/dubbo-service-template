# DEMO使用指南
## 配置Maven  
	将根目录的settings.xml拷贝到~/.m2/目录下，覆盖原来的文件

1. 环境准备

1.注册中心:

zookeeper(demo服务需要在注册中心注册);

2.API网关:

apigw(apigw会根据客户端调用,从注册中心中找到对应的服务,并进行服务转发);

2. 初始化

1.mvn package demo-api.jar,将jar包托管至API网关;

2.配置修改,修改demo-service/src/main/resources/config.properties;

#demo中有一个对数据库的简单操作,数据库链接句柄
com.sfebiz.demo.datasource.url=jdbc:mysql://127.0.0.1:3306/demo?autoReconnect=true&amp;useUnicode=true&amp;characterset=utf-8
#数据库用户名
com.sfebiz.demo.datasource.username=demo
#数据库密码
com.sfebiz.demo.datasource.password=demo
#日志输出目录
com.sfebiz.demo.log.home=/usr/admin/logs/demo
#注册中心地址
dubbo.registry.url=zookeeper://127.0.0.1:2181
#服务版本号,于API-GATEWAY保持一致
dubbo.reference.version=DEMO
#服务超时设置
dubbo.export.timeout=3000
3.数据库初始化脚本;

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `username` varchar(255) DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
INSERT INTO `demo` VALUES ('1', 'abc');
INSERT INTO `demo` VALUES ('2', 'def');
3.运行

在服务已成功发布至网关前提下(可以在apigw的文档页查看api是否以已经成功发布),通过运行demo-service/src/test/java下com.sfebiz.demo.service.test.Runner,启动dubbo服务;

4.模拟调用

1.客户端sdk生成：

apigw提供了客户端sdk生成工具,运行demo-service/src/test/java下ApiSdkJavaGeneratorTest,会将客户端sdk生成至指定目录(生成时需要指定生成目录以及包名demo-service/src/test/resources/config.properties中进行配置),此处请将生成目录指定为${dubbo-service-template}/demo-client/demo-client/src/main/java/com/sfebiz/demo/client;

2.自动化测试case编写：

完成客户端sdk生成,接下来可以进行一次完整的客户端调用模拟,demo参见demo-autotest/src/test/java下DemoTest.java.





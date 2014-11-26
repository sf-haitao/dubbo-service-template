package com.sfebiz.demo.service.ons;

/***************************************************
运行示例代码的前置条件：

 通过下面两种方式可以引入依赖(任选一种)
    1、Maven方式引入依赖
        <dependency>
          <groupId>com.aliyun.openservices</groupId>
          <artifactId>ons-client</artifactId>
          <version>1.1.1</version>
        </dependency>
        
    2、下载依赖Jar包
        http://onsall.oss-cn-hangzhou.aliyuncs.com/aliyun-ons-client-java.tar.gz
        
 注意：收发消息只能在阿里云ECS上进行
 ***************************************************/

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;


public class ConsumerClient {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, "CID_haitao_order_sys");//TODO 这里需要改成自己系统的consumerID
        properties.put(PropertyKeyConst.AccessKey, "0tNtH7eVTLLZFcpU");
        properties.put(PropertyKeyConst.SecretKey, "xsLm7BtZlbEbCC0j8LCz3LcGIKpAll");
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("topic_haitao_user-status_dev", "id_approve", new MessageListener() {

            public Action consume(Message message, ConsumeContext context) {
                try {
					System.out.println(new String(message.getBody(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
                return Action.CommitMessage;
            }
        });

       consumer.start();

       System.out.println("Consumer Started");
    }
}

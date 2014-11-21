package com.alibaba.ons.demo;

/***************************************************
????????????

 ??????????????(????)
    1?Maven??????
        <dependency>
          <groupId>com.aliyun.openservices</groupId>
          <artifactId>ons-client</artifactId>
          <version>1.1.1</version>
        </dependency>
        
    2?????Jar?
        http://onsall.oss-cn-hangzhou.aliyuncs.com/aliyun-ons-client-java.tar.gz
        
 ?????????????ECS???
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
        properties.put(PropertyKeyConst.ConsumerId, "CID_test1");
        properties.put(PropertyKeyConst.AccessKey, "0tNtH7eVTLLZFcpU");
        properties.put(PropertyKeyConst.SecretKey, "xsLm7BtZlbEbCC0j8LCz3LcGIKpAll");
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("test_top1", "tag1", new MessageListener() {

            public Action consume(Message message, ConsumeContext context) {
                System.out.println(message);
                try {
					System.out.println(new String(message.getBody(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                return Action.CommitMessage;
            }
        });

       consumer.start();

       System.out.println("Consumer Started");
    }
}

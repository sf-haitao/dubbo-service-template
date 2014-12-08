package com.sfebiz.demo.service.ocs;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 14/12/8 下午3:34
 */
public class OcsClient {

    public static void main(String[] args) throws Exception{

        String url = "2384b85c590211e4.m.cnqdalicm9pub001.ocs.aliyuncs.com:11211";
        String userName = "2384b85c590211e4";
        String passWord = "Hello1234";

        MemcachedClient memcachedClient;
        AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(userName, passWord));
        memcachedClient = new MemcachedClient(
                new ConnectionFactoryBuilder().setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                        .setAuthDescriptor(ad)
                        .build(),
                AddrUtil.getAddresses(url));
        if (null == memcachedClient) {
            throw new RuntimeException("can't initialize osc client!");
        }

        memcachedClient.set("test_key",3600,"test_value");
        System.out.println(memcachedClient.get("test_key"));
    }
}

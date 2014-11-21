package com.sfebiz.demo.client;

public class ApiConfig {
    public static final boolean isDebug           = true;
    // 是否对http请求启用gzip压缩，在服务端使用protobuf协议的情况下，能压缩到之前的80%
    public static final boolean useHttpGzip       = false;
    // 连接超时
    public static final int     connectionTimeout = 5000;
    // 请求超时
    public static final int     requestTimeout    = 30000;
    // 请求等待被重用的时间, 0 为禁用keepalive
    public static final int     keepaliveTime     = 5000;
    // web请求中使用的agent
    public static final String  webAgent          = "SFHT Android App";
    // 基础地址
    public static final String  baseUrl           = "http://115.28.145.123";
    // 多接口调用访问请求
    public static final String  apiUrl            = baseUrl + "/m.api";
}

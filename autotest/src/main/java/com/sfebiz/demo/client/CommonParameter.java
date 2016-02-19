package com.sfebiz.demo.client;

public final class CommonParameter {
    /**
     * Format 返回值格式，取值为枚举SerializeType中的定义
     */
    public static final String format = "_ft";

    /**
     * Location 用于返回信息国际化
     */
    public static final String location = "_lo";

    /**
     * token 代表访问者身份
     */
    public static final String token = "_tk";

    /**
     * device token 代表访问设备的身份
     */
    public static final String deviceToken = "_dtk";

    /**
     * method 请求的资源名
     */
    public static final String method = "_mt";

    /**
     * signature 参数字符串签名
     */
    public static final String signature = "_sig";

    /**
     * application id 应用编号
     */
    public static final String applicationId = "_aid";

    /**
     * call id 客户端调用编号
     */
    public static final String callId = "_cid";

    /**
     * business id 业务流水号，用于做幂等判断，风控等
     */
    public static final String businessId = "_bid";

    /**
     * device id 设备标示符
     */
    public static final String deviceId = "_did";

    /**
     * user id 用户标示符
     */
    public static final String userId = "_uid";

    /**
     * version code 客户端数字版本号
     */
    public static final String versionCode = "_vc";

    /**
     * signature method 签名算法 hmac,md5,sha1,rsa,ecc
     */
    public static final String signatureMethod = "_sm";

    /**
     * 动态密码验证对应的手机号
     */
    public static final String phoneNumber = "_pn";

    /**
     * 动态密码验证对应的动态码
     */
    public static final String dynamic = "_dyn";
}

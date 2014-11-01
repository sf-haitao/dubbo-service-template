// Auto Generated.  DO NOT EDIT!
package com.sfebiz.demo.client.api.request;

/**
 * 本类定义了接口有可能的返回值集合, 其中0为成功, 负数值为所有接口都有可能返回的通用code, 正数值是接口相关的code(请参见接口文档).
 */
public class ApiCode {
    
    /** 用户被锁定 服务端: */
    public static final int USER_LOCKED = -370;
    /** token错误 服务端: */
    public static final int TOKEN_ERROR = -360;
    /** 不是激活设备(用户在其他地方登录) 服务端: */
    public static final int NO_ACTIVE_DEVICE = -340;
    /** 不是用户的受信设备 服务端: */
    public static final int NO_TRUSTED_DEVICE = -320;
    /** token已过期 服务端: */
    public static final int TOKEN_EXPIRE = -300;
    /** 应用id不存在 服务端: */
    public static final int APPID_NOT_EXIST = -280;
    /** 上行短信尚未收到 服务端: */
    public static final int UPLINK_SMS_NOT_RECEIVED = -270;
    /** 手机号未绑定 服务端: */
    public static final int MOBILE_NOT_REGIST = -250;
    /** 请求解析错误 服务端: */
    public static final int REQUEST_PARSE_ERROR = -200;
    /** 非法的请求组合 服务端: */
    public static final int ILLEGAL_MULTIAPI_ASSEMBLY = -190;
    /** 签名错误 服务端: */
    public static final int SIGNATURE_ERROR = -180;
    /** 参数错误 服务端: */
    public static final int PARAMETER_ERROR = -140;
    /** mt参数服务端无法识别 服务端: */
    public static final int UNKNOWN_METHOD = -120;
    /** 服务端返回未知错误 服务端: */
    public static final int UNKNOWN_ERROR = -100;
    /** 成功 服务端: */
    public static final int SUCCESS = 0;
    /** 未分配返回值 服务端: */
    public static final int NO_ASSIGN = -2147483648;
    /** 手机动态密码错误 服务端: */
    public static final int DYNAMIC_CODE_ERROR = -260;
    /** 接口已升级 服务端: */
    public static final int API_UPGRADE = -220;
    /** 访问被拒绝 服务端: */
    public static final int ACCESS_DENIED = -160;
}
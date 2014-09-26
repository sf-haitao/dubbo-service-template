package com.sfebiz.demo.api;

import com.sfebiz.demo.entity.ComplexTestEntity;
import com.sfebiz.demo.entity.DemoReturnCode;
import com.sfebiz.demo.entity.SimpleTestEntity;
import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.DesignedErrorCode;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.ApiOpenState;
import net.pocrd.define.SecurityType;
import net.pocrd.responseEntity.JSONString;
import net.pocrd.responseEntity.RawString;

/**
 * Created by haomin on 2014/9/9.
 */
@ApiGroup(name = "demo", minCode = 1, maxCode = 1000000, codeDefine = DemoReturnCode.class, owner = "demo")
public interface DemoService {
    @HttpApi(name = "demo.sayHello", desc = "demo test", detail = "接口的详细说明", security = SecurityType.None, owner = "demo")
    public String sayHello(
            @ApiParameter(required = true, name = "name", desc = "say hello")
            String name);

    @HttpApi(name = "demo.testWeiXin", desc = "微信接口测试", detail = "接口的详细说明", security = SecurityType.RegisteredDevice, owner = "demo",
             state = ApiOpenState.OPEN_TO_WECHAT)
    RawString testWeiXin(
            @ApiParameter(required = false, name = "msg", desc = "test")
            String strValue);

    @HttpApi(name = "demo.testStructInput", desc = "结构化入参测试", detail = "接口的详细说明", security = SecurityType.UserLogin, owner = "demo")
    @DesignedErrorCode(DemoReturnCode._C_DEMO_UNKNOW_ERROR)
    SimpleTestEntity testStructInput(
            @ApiParameter(required = true, name = "simpleTestEntity", desc = "SimpleTestEntity结构化入参")
            SimpleTestEntity simpleTestEntity);

    @HttpApi(name = "demo.testCredits", desc = "测试积分下发", detail = "接口的详细说明", security = SecurityType.MobileOwner, owner = "demo")
    boolean testCredits();

    @HttpApi(name = "demo.testMsg", desc = "测试消息下发", detail = "接口的详细说明", security = SecurityType.None, owner = "demo")
    ComplexTestEntity testMsg();

    @HttpApi(name = "demo.testDesignedErrorCode", desc = "测试errorcode", detail = "接口的详细说明", security = SecurityType.None, owner = "demo")
    @DesignedErrorCode(DemoReturnCode._C_DEMO_UNKNOW_ERROR)
    boolean testDesignedErrorCode();

    @HttpApi(name = "demo.testErrorCode", desc = "测试errorcode", detail = "接口的详细说明", security = SecurityType.None, owner = "demo")
    boolean testErrorCode();

    @HttpApi(name = "demo.testRsaEncrypt", desc = "测试rsa加解密", detail = "接口的详细说明", security = SecurityType.None, owner = "demo")
    String testRsaEncrypt(
            @ApiParameter(required = true, rsaEncrypted = true, name = "param1", desc = "param1")
            String param1,
            @ApiParameter(required = false, rsaEncrypted = true, name = "param2", desc = "param2")
            String param2,
            @ApiParameter(required = true, rsaEncrypted = true, name = "param3", desc = "param3")
            SimpleTestEntity param3,
            @ApiParameter(required = false, rsaEncrypted = true, name = "param4", desc = "param4")
            SimpleTestEntity param4,
            @ApiParameter(required = true, name = "param5", desc = "param5")
            String param5,
            @ApiParameter(required = false, name = "param6", desc = "param6")
            String param6,
            @ApiParameter(required = true, rsaEncrypted = true, name = "param7", desc = "param7")
            int[] param7,
            @ApiParameter(required = false, rsaEncrypted = true, name = "param8", desc = "param8")
            int[] param8);

    @HttpApi(name = "demo.testJSONString", desc = "测试jsonString", detail = "接口的详细说明", security = SecurityType.None, owner = "demo")
    JSONString testJSONString();

    @HttpApi(name = "demo.testCid", desc = "测试_cid", detail = "接口的详细说明", security = SecurityType.None, owner = "demo")
    String testCid();
}
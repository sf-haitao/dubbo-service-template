package com.sfebiz.demo.api;

import com.sfebiz.demo.entity.DemoEntity;
import com.sfebiz.demo.entity.DemoReturnCode;
import net.pocrd.annotation.*;
import net.pocrd.define.CommonParameter;
import net.pocrd.define.SecurityType;

@ApiGroup(name = "demo", minCode = 1, maxCode = 1000000, codeDefine = DemoReturnCode.class, owner = "demo")
public interface DemoService {
    @HttpApi(name = "demo.sayHello", desc = "demo test", security = SecurityType.None, owner = "demo")
    public DemoEntity sayHello(
            @ApiParameter(required = true, name = "name", desc = "say hello")
            String name);

    @HttpApi(name = "demo.tryError", desc = "demo error", security = SecurityType.None, owner = "demo")
    public DemoEntity tryError(
            @ApiParameter(required = true, name = "in", desc = "input param")
            String in);

    @HttpApi(name = "demo.testRegistedDevice", desc = "demo registed device", security = SecurityType.RegisteredDevice, owner = "demo")
    @DesignedErrorCode(DemoReturnCode._C_DEMO_DEVICE_DENIED)
    public String testRegistedDevice();

    @HttpApi(name = "demo.testUserLogin", desc = "demo user login api", security = SecurityType.UserLogin, owner = "demo")
    public String testUserLogin(
            @ApiAutowired(CommonParameter.deviceId)
            long deviceId,
            @ApiAutowired(CommonParameter.userId)
            long userId);
}
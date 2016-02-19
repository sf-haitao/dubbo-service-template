package com.sfebiz.demo.api;

import com.sfebiz.demo.entity.DemoEntity;
import com.sfebiz.demo.entity.DemoReturnCode;
import com.sfebiz.demo.mock.MockDemoEntity;
import net.pocrd.annotation.*;
import net.pocrd.define.CommonParameter;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.ServiceException;

@ApiGroup(name = "demo", minCode = 1, maxCode = 1000000, codeDefine = DemoReturnCode.class, owner = "demo")
public interface DemoService {
    @HttpApi(name = "demo.sayHello", desc = "demo test", security = SecurityType.None, owner = "demo")
    public DemoEntity sayHello(
            @ApiParameter(required = true, name = "name", desc = "say hello")
            String name) throws ServiceException;

    @HttpApi(name = "demo.testMock", desc = "mock test", security = SecurityType.None, owner = "demo")
    @ApiMockReturnObject(MockDemoEntity.class)
    public DemoEntity testMock(
            @ApiParameter(required = true, name = "name", desc = "say hello")
            String name) throws ServiceException;

    @HttpApi(name = "demo.testShortCircuit", desc = "short circuit test", security = SecurityType.None, owner = "demo")
    @ApiShortCircuit(MockDemoEntity.class)
    public DemoEntity testShortCircuit(
            @ApiParameter(required = true, name = "name", desc = "say hello")
            String name) throws ServiceException;

    @HttpApi(name = "demo.tryError", desc = "demo error", security = SecurityType.None, owner = "demo")
    public DemoEntity tryError(
            @ApiParameter(required = true, name = "in", desc = "input param")
            String in) throws ServiceException;

    @HttpApi(name = "demo.testRegistedDevice", desc = "demo registed device", security = SecurityType.RegisteredDevice, owner = "demo")
    @DesignedErrorCode(DemoReturnCode._C_DEMO_DEVICE_DENIED)
    public String testRegistedDevice() throws ServiceException;

    @HttpApi(name = "demo.testUserLogin", desc = "demo user login api", security = SecurityType.UserLogin, owner = "demo")
    public String testUserLogin(
            @ApiAutowired(CommonParameter.deviceId)
            long deviceId,
            @ApiAutowired(CommonParameter.userId)
            long userId) throws ServiceException;

    @HttpApi(name = "demo.getResByThirdPartyId", desc = "demo getResByThirdPartyId", security = SecurityType.Integrated, owner = "demo",
            allowThirdPartyIds = { 1 })
    public String getResByThirdPartyId(
            @ApiParameter(required = true, name = "in", desc = "输入参数")
            String something) throws ServiceException;

    @HttpApi(name = "demo.testRedirect", desc = "test redirect", security = SecurityType.None, owner = "demo")
    public String testRedirect() throws ServiceException;

    @HttpApi(name = "demo.testIgnoreParameterForSecurity", desc = "test redirect", security = SecurityType.None, owner = "demo")
    public String testIgnoreParameterForSecurity(
            @ApiParameter(required = true, name = "in", desc = "输入参数", ignoreForSecurity = true)
            String something
    ) throws ServiceException;
}
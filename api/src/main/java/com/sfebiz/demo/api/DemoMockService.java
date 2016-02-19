package com.sfebiz.demo.api;

import com.sfebiz.demo.entity.DemoEntity;
import com.sfebiz.demo.entity.DemoReturnCode;
import com.sfebiz.demo.mock.MockServiceImpl;
import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiMockInterfaceImpl;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.ServiceException;

/**
 * Created by rendong on 15/8/5.
 */
@ApiGroup(name = "demo", minCode = 1, maxCode = 1000000, codeDefine = DemoReturnCode.class, owner = "demo")
@ApiMockInterfaceImpl(MockServiceImpl.class)
public interface DemoMockService {
    @HttpApi(name = "demo.testMockService", desc = "mock service test", security = SecurityType.None, owner = "demo")
    public DemoEntity testMockService(
            @ApiParameter(required = true, name = "name", desc = "say hello")
            String name) throws ServiceException;
}
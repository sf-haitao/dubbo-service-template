package com.sfebiz.demo.api;

import com.sfebiz.demo.entity.DemoEntity;
import com.sfebiz.demo.entity.DemoReturnCode;
import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.SecurityType;

@ApiGroup(name = "demo", minCode = 1, maxCode = 1000000, codeDefine = DemoReturnCode.class, owner = "demo")
public interface DemoService {
    @HttpApi(name = "demo.sayHello", desc = "demo test", security = SecurityType.None, owner = "demo")
    public DemoEntity sayHello(
            @ApiParameter(required = true, name = "name", desc = "say hello")
            String name);
}
package com.sfebiz.demo.autotest.test;

import com.sfebiz.demo.autotest.test.WebRequestUtil.ResponseFiller;
import com.sfebiz.demo.client.ApiConfig;
import com.sfebiz.demo.client.ApiContext;
import com.sfebiz.demo.client.BaseRequest;
import com.sfebiz.demo.client.ServerResponse;
import com.sfebiz.demo.client.api.request.Demo_SayHello;
import com.sfebiz.demo.client.api.request.Demo_TestUserLogin;
import com.sfebiz.demo.client.api.request.Demo_TryError;
import com.sfebiz.demo.client.api.resp.Api_DEMO_DemoEntity;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by guankaiqiang521 on 2014/9/29.
 */
public class DemoTest {
    private static final String url          = ApiConfig.apiUrl;
    private static final long   deviceId     = 1414807058834L;
    private static final String deviceSecret = "581bb3c7f2d09e4d2f07f69706fff13f261f4cfa2038cd2ab7bb46040ca2d568";
    private static final String deviceToken  = "jxpvuVNWcYb75UlLHC3QyptGUwn0V+LDzdi/GMTLcmGN1rmpX80ze7hRE8peb0dbjfUWi52dEoaZy6YCJZcF9L4f+2gJXMjncRCFhGY3AHo=";
    private static final long   userId       = 21L;
    private static final String userToken    = "vPVQUVm8fP9sJ0Bu3gxbT/U9D1KcVfMklq+Y6DUDwIUxRkNTapEv+RlUyE95XTA9K5LPgIYW7UOOltOR8ZOsBJi1p/hkudlVJkWfiWXWUX4=";

    private void initWithDeviceInfo(ApiContext context) {
        context.setDeviceInfo(deviceId, deviceSecret, deviceToken);
    }

    private void initWithUserInfo(ApiContext context) {
        initWithDeviceInfo(context);
        context.setUserInfo(userId, userToken);
    }

    @Test
    public void sayHelloTest() {
        final ApiContext context = new ApiContext("1", 123);
        Demo_SayHello sayHello = new Demo_SayHello("abc");
        final BaseRequest[] requests = new BaseRequest[] { sayHello };
        WebRequestUtil.fillResponse(url, context.getParameterString(requests), String.valueOf(System.currentTimeMillis()), true,
                new ResponseFiller() {
                    public ServerResponse fill(InputStream is) {
                        return context.fillResponse(requests, is);
                    }
                });
        Api_DEMO_DemoEntity resp = sayHello.getResponse();
        Assert.assertEquals(resp.id, 1);
        Assert.assertEquals(resp.name, "abc");
    }

    @Test
    public void tryErrorTest() {
        final ApiContext context = new ApiContext("1", 123);
        Demo_TryError tryError = new Demo_TryError("abc");
        final BaseRequest[] requests = new BaseRequest[] { tryError };
        WebRequestUtil.fillResponse(url, context.getParameterString(requests), String.valueOf(System.currentTimeMillis()), true,
                new ResponseFiller() {
                    public ServerResponse fill(InputStream is) {
                        return context.fillResponse(requests, is);
                    }
                });
        Assert.assertEquals(tryError.getReturnCode(), -100);
    }

    @Test
    public void testUserLogin() {
        final ApiContext context = new ApiContext("1", 123);
        Demo_TestUserLogin userLogin = new Demo_TestUserLogin();
        final BaseRequest[] requests = new BaseRequest[] { userLogin };
        WebRequestUtil.fillResponse(url, context.getParameterString(requests), String.valueOf(System.currentTimeMillis()), true,
                new ResponseFiller() {
                    public ServerResponse fill(InputStream is) {
                        return context.fillResponse(requests, is);
                    }
                });
        System.out.println(userLogin.getResponse().value);
    }
}

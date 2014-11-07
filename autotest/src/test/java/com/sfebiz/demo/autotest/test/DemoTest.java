package com.sfebiz.demo.autotest.test;

import com.sfebiz.demo.autotest.test.WebRequestUtil.ResponseFiller;
import com.sfebiz.demo.client.ApiConfig;
import com.sfebiz.demo.client.ApiContext;
import com.sfebiz.demo.client.BaseRequest;
import com.sfebiz.demo.client.ServerResponse;
import com.sfebiz.demo.client.api.request.*;
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
    private static final long   userId       = 22L;
    private static final String userToken    = "F3Ul7EEwE0wUwZWlXYqZqf1l476xWq/yzmAFWWnjGchKZBnu2Sgb8dz4VnYgEy3F+Hfp/nxbb3vxlOkPi8Syb+U/BU1h8U+THkNSgHdU9UM=";

    private void initWithDeviceInfo(ApiContext context) {
        context.setDeviceInfo(deviceId, deviceSecret, deviceToken);
    }

    private void initWithUserInfo(ApiContext context) {
        initWithDeviceInfo(context);
        context.setUserInfo(userId, userToken, Long.MAX_VALUE);
    }

    @Test
    public void sayHelloTest() {
        final ApiContext context = new ApiContext("1", 123);
        final Demo_SayHello sayHello = new Demo_SayHello("abc");
        WebRequestUtil.fillResponse(url, context.getParameterString(sayHello), String.valueOf(System.currentTimeMillis()), true,
                new ResponseFiller() {
                    public ServerResponse fill(InputStream is) {
                        return context.fillResponse(sayHello, is);
                    }
                });
        Api_DEMO_DemoEntity resp = sayHello.getResponse();
        Assert.assertEquals(ApiCode.SUCCESS, sayHello.getReturnCode());
        Assert.assertEquals(resp.id, 1);
        Assert.assertEquals(resp.name, "abc");
    }

    @Test
    public void tryErrorTest() {
        final ApiContext context = new ApiContext("1", 123);
        final Demo_TryError tryError = new Demo_TryError("abc");
        WebRequestUtil.fillResponse(url, context.getParameterString(tryError), String.valueOf(System.currentTimeMillis()), true,
                new ResponseFiller() {
                    public ServerResponse fill(InputStream is) {
                        return context.fillResponse(tryError, is);
                    }
                });
        Assert.assertEquals(-100, tryError.getReturnCode());
    }

    @Test
    public void testRegistedDevice() {
        final ApiContext context = new ApiContext("1", 123);
        initWithDeviceInfo(context);
        final Demo_TestRegistedDevice regiestedDevice = new Demo_TestRegistedDevice();
        WebRequestUtil.fillResponse(url, context.getParameterString(regiestedDevice), String.valueOf(System.currentTimeMillis()), true,
                new ResponseFiller() {
                    public ServerResponse fill(InputStream is) {
                        return context.fillResponse(regiestedDevice, is);
                    }
                });
        Assert.assertEquals(ApiCode.SUCCESS, regiestedDevice.getReturnCode());
    }

    @Test
    public void testUserLogin() {
        final ApiContext context = new ApiContext("1", 123);
        initWithUserInfo(context);
        Demo_TestUserLogin userLogin = new Demo_TestUserLogin();
        final BaseRequest[] requests = new BaseRequest[] { userLogin };
        WebRequestUtil.fillResponse(url, context.getParameterString(requests), String.valueOf(System.currentTimeMillis()), true,
                new ResponseFiller() {
                    public ServerResponse fill(InputStream is) {
                        return context.fillResponse(requests, is);
                    }
                });
        System.out.println(userLogin.getResponse().value);
        Assert.assertEquals(ApiCode.SUCCESS, userLogin.getReturnCode());
    }
}

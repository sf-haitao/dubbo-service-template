package com.sfebiz.demo.autotest.test;

import com.sfebiz.demo.autotest.test.WebRequestUtil.ResponseFiller;
import com.sfebiz.demo.client.ApiContext;
import com.sfebiz.demo.client.BaseRequest;
import com.sfebiz.demo.client.ServerResponse;
import com.sfebiz.demo.client.api.request.Demo_SayHello;
import com.sfebiz.demo.client.api.request.Demo_TryError;
import com.sfebiz.demo.client.api.resp.Api_DEMO_DemoEntity;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by guankaiqiang521 on 2014/9/29.
 */
public class DemoTest {
    private static final String url        = "http://localhost:8080/m.api";
    private static final String RSA_PRIKEY = "rsa private key";
    @Test
    public void sayHelloTest() {
        final ApiContext context = new ApiContext("1", 123, RSA_PRIKEY);
        Demo_SayHello sayHello = new Demo_SayHello("abc");
        final BaseRequest[] requests = new BaseRequest[]{sayHello};
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
        final ApiContext context = new ApiContext("1", 123, RSA_PRIKEY);
        Demo_TryError tryError = new Demo_TryError("abc");
        final BaseRequest[] requests = new BaseRequest[]{tryError};
        WebRequestUtil.fillResponse(url, context.getParameterString(requests), String.valueOf(System.currentTimeMillis()), true,
                                    new ResponseFiller() {
                                        public ServerResponse fill(InputStream is) {
                                            return context.fillResponse(requests, is);
                                        }
                                    });
        Assert.assertEquals(tryError.getReturnCode(), -100);
    }
}

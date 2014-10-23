package com.sfebiz.demo.autotest.test;

import com.sfebiz.demo.autotest.test.WebRequestUtil.ResponseFiller;
import com.sfebiz.demo.client.ApiContext;
import com.sfebiz.demo.client.BaseRequest;
import com.sfebiz.demo.client.ServerResponse;
import com.sfebiz.demo.client.api.request.Demo_SayHello;
import com.sfebiz.demo.client.api.request.Demo_TestUserLogin;
import com.sfebiz.demo.client.api.request.Demo_TryError;
import com.sfebiz.demo.client.api.resp.Api_DEMO_DemoEntity;
import com.sfebiz.demo.client.util.Base64Util;
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

    @Test
    public void testUserLogin() {
        final String utk = "3So+gESObR5nUubHYPhdvXmyq/FX6EJNOt3ErvMhVvGxwD8cpsDr+nc22onZg7UJ";//这个应该调用userLogin服务获取到，目前先使用伪造token。
        final String dtk = "3hflsHoc4SpQsxcz0hB8glKe/33LWjLY9PerSVILnuZSxz4ycLxF2X4+sdDEXUpq";//设备token，由deviceRegister接口获取
        final byte[] cert = Base64Util.decode(
                "MIIFRwIBAzCCBQEGCSqGSIb3DQEHAaCCBPIEggTuMIIE6jCCAR8GCSqGSIb3DQEHAaCCARAEggEMMIIBCDCCAQQGCyqGSIb3DQEMCgECoIGwMIGtMCgGCiqGSIb3DQEMAQMwGgQUJTf+aGnqu9skuK2kUhnxzD4nDvoCAgQABIGAH6KHE7VuW5yFhW+GxO3Mu04DhYg3QIUcAGiF/1JnDRBKd8y20wYatsqrHoxALmOS+wwBxs34sw+F2vjHSGDAWUn9qUJUZxDGjHTuLvh6lR1AYZAQoljco5UWpIvaL6O9Z89raTNL8o5Kp0VmtiVYW/8po/P6+ionR01I2ZZivdsxQjAbBgkqhkiG9w0BCRQxDh4MAGMAbABpAGUAbgB0MCMGCSqGSIb3DQEJFTEWBBSBK7EoTh5ZhoU/k1nfFnDnTtofxjCCA8MGCSqGSIb3DQEHBqCCA7QwggOwAgEAMIIDqQYJKoZIhvcNAQcBMCgGCiqGSIb3DQEMAQYwGgQUBcoahhVdHSZ+oQbQtojL6hsRQXECAgQAgIIDcO3MmIlibwEKZ2HdOe0YKcibBsytmH75acc5+Aj/bZsTsIbMljTpa16xnoMaYgPG6646u2R3aPL4rB0megN5n0gY4iOnoqwFwrL9RqIE3RZjELvy50kE+5C/+BYk7vGQB20Fu1ydwCNDI1uA5AmnPRLiz6xZQFDa23fhO6y1XDKPC2a7xr/SN9MF4KUS8LwJ34hAAraA9s7BJA3PAOZ9FWuesrSm1tm+0UY4Og4RploCXFUFKLh6vQyHIO8GssY3aQPl57tlTxKRf1ryDraSghPKstEtw3i4aMmx3F+t2H9UJje4XCttxZZs/mIpY3UXT8JRNt1ilcZb8v55jAmz74sptUMw4AFsog57M73kxGPeo6n4KsfAaL2mDTki7kI55q0zTEsrAcSGBmWJYEk8NXvJZSOFILFEiATbFUhaVcMEF0wfBwGfmdos/Wo+vKkQZr611NI9ftiGYVHlEQQ8ZTN93o6h3eekDFHMUFZAa91rBKbuMeO782XPOxTkeo0fPjgzaTdjECUg3VMBN7kOHeyy1ty064r3/uXdpa7JGnHcc2Y7UvcYWu9QFBqTntVD6zhhWcT6UmNZfpmK0xRWGNVEDrH7BONnYzehhtC99m9woOW0iK22t2Jg+Gbb/bGBady2lfMmHbTS/iNMgukbl96lCZJrzicG5Gf4KHQfZAh9xWR8RmsIFCbDfpBw3+FcfjHoZ1FXghzJ8oet2dKwB8nSD+OMBD0jV4MJogEpRVLykUZ7N+PPIr4UJJVJOcqtnQL0Zr6Dz1K8r4WhBEkTJpmV0UY+G8NfCvTn4KatxDckNh9906zDiq1YuIqZccOJghTtgVm8V45ExkNHVaieeiSEx/Z3TBp/ggv1kBNOMmkWMWBZKp8JyoENhwIdXxl7vCtQoTQjjKY0Mk+zZdYZk7GcLEriv/94b5IiT4bFZs+3BTVWIshxAnHD+aKH9yGK8AgQNcCm5sSNNz6yYgGy6w3H7SeRs8syWxhb1dEUWViNuquXI0dfUAq6bBVLAoRTnpvq208KQ0xKYsLOXZdvYk60m4oEjbmpsjXaBLb1y0Q4Hx0v8rtUB2YEhUMo4UjQa9w3P6WClLxnyr7iS+f03rrCDuWl2XYSt+NgzHfokM8khtSq7oJCC3KSlVSARXtrOhnVDYgAib7D8SS4TsUTPmUwPTAhMAkGBSsOAwIaBQAEFIOuj+/MYo/s35PwaMYMtTgAFsmNBBTVclbVjqVxr2aSG2jMofVsyfvtxwICBAA=");//签名验证方式尚未确定，先使用老证书进行数字签名，待敲定签名方式需要重构客户端签名逻辑。此处使用证书能够顺利进行数字签名。
        final ApiContext context = new ApiContext("1", 123, RSA_PRIKEY);
        context.setToken(utk);
        context.setCertificateWithDeviceToken(cert, dtk);
        Demo_TestUserLogin userLogin = new Demo_TestUserLogin();
        final BaseRequest[] requests = new BaseRequest[]{userLogin};
        WebRequestUtil.fillResponse(url, context.getParameterString(requests), String.valueOf(System.currentTimeMillis()), true,
                                    new ResponseFiller() {
                                        public ServerResponse fill(InputStream is) {
                                            return context.fillResponse(requests, is);
                                        }
                                    });
        System.out.println(userLogin.getResponse().value);
    }
}

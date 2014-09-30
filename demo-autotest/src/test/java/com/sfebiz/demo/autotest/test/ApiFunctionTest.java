package com.sfebiz.demo.autotest.test;

import com.sfebiz.demo.autotest.test.WebRequestUtil.ResponseFiller;
import com.sfebiz.demo.client.ApiContext;
import com.sfebiz.demo.client.BaseRequest;
import com.sfebiz.demo.client.ServerResponse;
import com.sfebiz.demo.client.api.request.Demo_SayHello;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by guankaiqiang521 on 2014/9/29.
 */
public class ApiFunctionTest {
    private static final String url = "http://localhost:8080/m.api";
    private static final String rsa = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDoIjY+VacM/v0q47oQkbE4eVo4AS/Px07EMCmlYmRjY9x1OeippSppQ1eNRIuFCbZRqpMoayDO68UdWPCSqOt1I8Uw03MzVDmy38ZBo6dVTRrqWW9z7vbQQ1nWkEcUWcRTIQIktQ2ptO4AOlZa1x1/zvsNBodTNqhqCGPeTNUwyQIDAQAB";
    @Test
    public void sayHelloTest() {
        final ApiContext context = new ApiContext("1", 123, rsa);
        Demo_SayHello sayHello = new Demo_SayHello("abc");
        final BaseRequest[] requests = new BaseRequest[]{sayHello};
        WebRequestUtil.fillResponse(url, context.getParameterString(requests), String.valueOf(System.currentTimeMillis()), true, new ResponseFiller() {
            public ServerResponse fill(InputStream is) {
                return context.fillResponse(requests, is);
            }
        });
    }
}

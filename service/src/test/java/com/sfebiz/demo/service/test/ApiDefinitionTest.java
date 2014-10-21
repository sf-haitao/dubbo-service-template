package com.sfebiz.demo.service.test;

import com.sfebiz.demo.api.DemoService;
import com.sfebiz.demo.service.http.DemoServiceImpl;
import net.pocrd.core.ApiDocumentationHelper;
import net.pocrd.core.ApiManager;
import net.pocrd.entity.ApiMethodInfo;
import org.junit.Test;

import java.util.List;

/**
 * Created by guankaiqiang521 on 2014/9/29.
 */
public class ApiDefinitionTest {
    @Test
    public void testApi() {
        ApiManager manager = new ApiManager();
        ApiDocumentationHelper apiDoc = new ApiDocumentationHelper();
        List<ApiMethodInfo> apis = null;
        apis = ApiManager.parseApi(DemoService.class, new DemoServiceImpl());
        apiDoc.getDocument(apis.toArray(new ApiMethodInfo[apis.size()]));
        manager.register(apis);
        System.out.println(apis.size());
    }
}

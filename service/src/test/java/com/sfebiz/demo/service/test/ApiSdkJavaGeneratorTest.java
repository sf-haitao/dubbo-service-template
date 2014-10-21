package com.sfebiz.demo.service.test;

import com.sfebiz.demo.api.DemoService;
import com.sfebiz.demo.service.http.DemoServiceImpl;
import net.pocrd.core.ApiDocumentationHelper;
import net.pocrd.core.ApiManager;
import net.pocrd.core.generator.ApiSdkJavaGenerator;
import net.pocrd.define.Serializer;
import net.pocrd.document.Document;
import net.pocrd.entity.ApiMethodInfo;
import net.pocrd.entity.CodeGenConfig;
import net.pocrd.util.POJOSerializerProvider;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by guankaiqiang521 on 2014/9/29.
 */
public class ApiSdkJavaGeneratorTest {
    @Test
    public void testJavaGenertor() throws ParserConfigurationException, IOException, SAXException {
        Properties prop = new Properties();
        prop.setProperty("net.pocrd.apiSdkJavaLocation", "/myworkspace/dubbo-service-template/demo-autotest/src/main/java/com/sfebiz/demo/client");
        prop.setProperty("net.pocrd.apiSdkJavaPkgName", "com.sfebiz.demo.client");
        CodeGenConfig.init(prop);
        List<ApiMethodInfo> infoList = ApiManager.parseApi(DemoService.class, new DemoServiceImpl());
        ApiMethodInfo[] array = new ApiMethodInfo[infoList.size()];
        infoList.toArray(array);
        Serializer<Document> docs = POJOSerializerProvider.getSerializer(Document.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        docs.toXml(new ApiDocumentationHelper().getDocument(array), outputStream, true);
        ByteArrayInputStream swapStream = new ByteArrayInputStream(outputStream.toByteArray());
        ApiSdkJavaGenerator.getInstance().generate(swapStream);
    }
}

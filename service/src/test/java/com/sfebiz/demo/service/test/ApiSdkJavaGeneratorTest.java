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
import java.util.*;

/**
 * Created by guankaiqiang521 on 2014/9/29.
 */
public class ApiSdkJavaGeneratorTest {
    @Test
    public void testJavaGenertor() throws ParserConfigurationException, IOException, SAXException {

//        ArrayList<Integer> c = new ArrayList<Integer>(){};
//        c.add(-370);
//        c.add(0);
//        c.add(Integer.MIN_VALUE);
//        c.add(1);
//        Collections.sort(c, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o, Integer o2) {
//
//                return o.intValue() > o2.intValue() ? 1 : o.intValue() < o2.intValue() ? -1 : 0;
//            }
//        });
        Properties prop = new Properties();
        prop.setProperty("net.pocrd.apiSdkJavaLocation", "/Users/rendong/workspace/service/dubbo-service-template/autotest/src/main/java/com/sfebiz/demo/client");
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

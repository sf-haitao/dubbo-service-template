package com.sfebiz.demo.service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.sfebiz.demo.api.DemoService;
import com.sfebiz.demo.entity.ComplexTestEntity;
import com.sfebiz.demo.entity.DemoReturnCode;
import com.sfebiz.demo.entity.SimpleTestEntity;
import net.pocrd.define.CommonParameter;
import net.pocrd.dubboext.DubboExtProperty;
import net.pocrd.entity.ApiReturnCode;
import net.pocrd.responseEntity.CreditNotification;
import net.pocrd.responseEntity.JSONString;
import net.pocrd.responseEntity.MessageNotification;
import net.pocrd.responseEntity.RawString;
import org.slf4j.MDC;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by guankaiqiang521 on 2014/9/16.
 */
public class DemoServiceImpl implements DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
    @Override
    public String sayHello(String name) {
        logger.info("say hello to " + name);
        return "hello , " + name + " !";
    }
    static class WeChatEntity {
        public String toUserName;

        public String fromUserName;
        public String msgType;
        WeChatEntity(String to, String from, String type) {
            this.toUserName = to;
            this.fromUserName = from;
            this.msgType = type;
        }
    }
    public static final WeChatEntity parseMsg(String str) throws ParserConfigurationException, SAXException, IOException {
        return parseMsg(new StringReader(str));
    }
    private static ThreadLocal<DocumentBuilder> xmlBuilder = new ThreadLocal<DocumentBuilder>();

    public static final WeChatEntity parseMsg(Reader xmlReader) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder db = xmlBuilder.get();
        if (db == null) {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmlBuilder.set(db);
        } else {
            db.reset();
        }
        InputSource is = new InputSource();
        is.setCharacterStream(xmlReader);
        Document doc = db.parse(is);
        return new WeChatEntity(getElementValue(doc, "ToUserName", true), getElementValue(doc, "FromUserName", true),
                                getElementValue(doc, "MsgType", true));
    }
    private static String getElementValue(Document doc, String name, boolean isRequired) {
        NodeList nl = doc.getElementsByTagName(name);
        if (nl == null || nl.getLength() == 0) {
            if (isRequired) {
                throw new RuntimeException("missing required field. " + name);
            } else {
                return null;
            }
        }
        String v = nl.item(0).getFirstChild().getNodeValue();
        return v;
    }
    @Override
    public RawString testWeiXin(String msg) {
        RawString rawString = null;
        try {
            WeChatEntity weChatEntity = parseMsg(msg);
            StringBuilder writer = new StringBuilder();
            writer.append("<xml>");
            writer.append("<ToUserName><![CDATA[");
            writer.append(weChatEntity.fromUserName);
            writer.append("]]></ToUserName><FromUserName><![CDATA[");
            writer.append(weChatEntity.toUserName);
            writer.append("]]></FromUserName><CreateTime>");
            writer.append("" + System.currentTimeMillis() / 1000);
            writer.append("</CreateTime><MsgType><![CDATA[");
            writer.append(weChatEntity.msgType);
            writer.append("]]></MsgType>");
            writer.append("<Content><![CDATA[");
            writer.append("建设中,请期待...");
            writer.append("]]></Content>");
            writer.append("</xml>");
            rawString = new RawString();
            rawString.value = writer.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            DubboExtProperty.setErrorCode(ApiReturnCode.INTERNAL_SERVER_ERROR);
        } finally {
            return rawString;
        }
    }
    @Override
    public SimpleTestEntity testStructInput(SimpleTestEntity simpleTestEntity) {
        return simpleTestEntity;
    }
    @Override
    public boolean testCredits() {
        CreditNotification creditNotification = new CreditNotification();
        creditNotification.credit = 1;
        creditNotification.description = "发积分啦!!!!";
        DubboExtProperty.addCreditsGain(creditNotification);
        DubboExtProperty.addCreditsGain(creditNotification);
        DubboExtProperty.addCreditsGain(creditNotification);
        return false;
    }
    @Override
    public ComplexTestEntity testMsg() {
        MessageNotification msgNotification = new MessageNotification();
        msgNotification.content = "你收到一条消息";
        DubboExtProperty.addMessageInfo(msgNotification);
        DubboExtProperty.addMessageInfo(msgNotification);
        CreditNotification creditNotification = new CreditNotification();
        creditNotification.credit = 1;
        creditNotification.description = "发积分啦!!!!";
        DubboExtProperty.addMessageInfo(msgNotification);
        DubboExtProperty.addCreditsGain(creditNotification);
        ComplexTestEntity e = new ComplexTestEntity();
        e.boolValue = true;
        e.floatValue = 1.234f;
        e.simpleTestEntity = new SimpleTestEntity();
        e.simpleTestEntity.intArray = new int[]{1,2,3};
        e.simpleTestEntityList = new ArrayList<SimpleTestEntity>();
        e.simpleTestEntityList.add(new SimpleTestEntity());
        e.simpleTestEntityList.add(new SimpleTestEntity());
        return e;
    }
    @Override
    public boolean testDesignedErrorCode() {
        DubboExtProperty.setErrorCode(DemoReturnCode.DEMO_UNKNOW_ERROR);
        return false;
    }
    @Override
    public boolean testErrorCode() {
        DubboExtProperty.setErrorCode(DemoReturnCode.DEMO_UNKNOW_ERROR);
        return false;
    }

    @Override
    public String testRsaEncrypt(String param1, String param2, SimpleTestEntity param3, SimpleTestEntity param4, String param5, String param6,
                                 int[] param7, int[] param8) {
        System.out.println(
                param1 + " " + param2 + " " + param3.strValue + " " + param4 != null ? param4.strValue : "" + " " + param5 + " " + param6 + " " + param7 + " " + param8);
        return param1 + " " + param2 + " " + param3.strValue + " " + param4 != null ? param4.strValue : "" + " " + param5 + " " + param6 + " " + param7 + " " + param8;
    }
    @Override
    public JSONString testJSONString() {
        JSONString jsonString = new JSONString();
        jsonString.value = "{\"json\":\"value\"}";
        return jsonString;
    }

    @Override
    public String testCid() {
        return MDC.get(CommonParameter.callId);
    }
}

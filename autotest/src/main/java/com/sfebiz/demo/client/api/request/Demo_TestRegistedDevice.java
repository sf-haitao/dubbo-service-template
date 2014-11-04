// Auto Generated.  DO NOT EDIT!
    
package com.sfebiz.demo.client.api.request;

import com.sfebiz.demo.client.LocalException;
import com.sfebiz.demo.client.BaseRequest;
import com.sfebiz.demo.client.SecurityType;
import com.sfebiz.demo.client.api.resp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

/**
 * demo registed device
 * 
 * @author demo
 *
 */
public class Demo_TestRegistedDevice extends BaseRequest<Api_StringResp> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     */
    public Demo_TestRegistedDevice() {
        super("demo.testRegistedDevice", SecurityType.RegisteredDevice);
        
    }
    /**
     * 当前请求有可能的异常返回值
     */
    public int handleError() {
        switch (response.code) {
            // device denied
            case ApiCode.DEMO_DEVICE_DENIED_100: {
                break;
            }
        }
        return response.code;
    }
    
    /**
     * 不要直接调用这个方法，API使用者应该访问基类的getResponse()获取接口的返回值
     */
    @Override
    protected Api_StringResp getResult(JSONObject json) {        
        try {
            return Api_StringResp.deserialize(json);
        } catch (Exception e) {
            logger.error(e, "Api_StringResp deserialize failed.");
        }
        return null;
    }
}
  
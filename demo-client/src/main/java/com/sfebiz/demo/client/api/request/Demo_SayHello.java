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
 * demo test
 * 
 * @author demo
 *
 */
public class Demo_SayHello extends BaseRequest<Api_DEMO_DemoEntity> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     * @param name say hello
     */
    public Demo_SayHello(String name) {
        super("demo.sayHello", SecurityType.None);
        
        try {
            params.put("name", name);
        } catch(Exception e) {
            throw new LocalException(e, "SERIALIZE_ERROR", LocalException.SERIALIZE_ERROR);
        }
        
    }
    /**
     * 当前请求有可能的异常返回值
     */
    public int handleError() {
        switch (response.code) {
        }
        return response.code;
    }
    
    /**
     * 不要直接调用这个方法，API使用者应该访问基类的getResponse()获取接口的返回值
     */
    @Override
    protected Api_DEMO_DemoEntity getResult(JSONObject json) {        
        try {
            return Api_DEMO_DemoEntity.deserialize(json);
        } catch (Exception e) {
            logger.error(e, "Api_DEMO_DemoEntity deserialize failed.");
        }
        return null;
    }
}
  
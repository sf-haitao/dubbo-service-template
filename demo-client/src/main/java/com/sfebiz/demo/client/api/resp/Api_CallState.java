// Auto Generated.  DO NOT EDIT!
package com.sfebiz.demo.client.api.resp;
    
import org.json.JSONException;
import org.json.JSONObject;

public class Api_CallState {

    /**
     * 返回值
     */
    public int code;
      
    /**
     * 数据长度
     */
    public int length;
      
    /**
     * 返回信息
     */
    public String msg;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_CallState deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_CallState deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Api_CallState result = new Api_CallState();
            
            // 返回值
            result.code = json.optInt("code");
            // 数据长度
            result.length = json.optInt("length");
            // 返回信息
            
              if(!json.isNull("msg")){
                  result.msg = json.optString("msg", null);
              }
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 返回值
        json.put("code", this.code);
          
        // 数据长度
        json.put("length", this.length);
          
        // 返回信息
        if(this.msg != null) { json.put("msg", this.msg); }
          
        return json;
    }
}
  
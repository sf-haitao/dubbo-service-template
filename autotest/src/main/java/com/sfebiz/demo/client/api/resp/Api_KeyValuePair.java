// Auto Generated.  DO NOT EDIT!
package com.sfebiz.demo.client.api.resp;
    
import org.json.JSONException;
import org.json.JSONObject;

public class Api_KeyValuePair {

    /**
     * 键
     */
    public String key;
      
    /**
     * 值
     */
    public String value;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_KeyValuePair deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_KeyValuePair deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Api_KeyValuePair result = new Api_KeyValuePair();
            
            // 键
            
              if(!json.isNull("key")){
                  result.key = json.optString("key", null);
              }
            // 值
            
              if(!json.isNull("value")){
                  result.value = json.optString("value", null);
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
        
        // 键
        if(this.key != null) { json.put("key", this.key); }
          
        // 值
        if(this.value != null) { json.put("value", this.value); }
          
        return json;
    }
}
  
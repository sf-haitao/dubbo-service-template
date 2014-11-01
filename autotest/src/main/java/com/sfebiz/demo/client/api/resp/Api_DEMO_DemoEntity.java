// Auto Generated.  DO NOT EDIT!
package com.sfebiz.demo.client.api.resp;
    
import org.json.JSONException;
import org.json.JSONObject;

public class Api_DEMO_DemoEntity {

    /**
     * id
     */
    public int id;
      
    /**
     * name
     */
    public String name;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_DEMO_DemoEntity deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_DEMO_DemoEntity deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Api_DEMO_DemoEntity result = new Api_DEMO_DemoEntity();
            
            // id
            result.id = json.optInt("id");
            // name
            
              if(!json.isNull("name")){
                  result.name = json.optString("name", null);
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
        
        // id
        json.put("id", this.id);
          
        // name
        if(this.name != null) { json.put("name", this.name); }
          
        return json;
    }
}
  
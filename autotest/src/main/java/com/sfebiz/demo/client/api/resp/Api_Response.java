// Auto Generated.  DO NOT EDIT!
package com.sfebiz.demo.client.api.resp;
    
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Api_Response {

    /**
     * 当前服务端时间
     */
    public long systime;
      
    /**
     * 调用返回值
     */
    public int code;
      
    /**
     * 调用标识符
     */
    public String cid;
      
    /**
     * 用作特定场景使用
     */
    public String data;
      
    /**
     * API调用状态，code的信息请参考ApiCode定义文件
     */
    public List<Api_CallState> stateList;
    /**
     * 服务端返回的通知事件集合
     */
    public List<Api_KeyValuePair> notificationList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_Response deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_Response deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Api_Response result = new Api_Response();
            
            // 当前服务端时间
            result.systime = json.optLong("systime");
            // 调用返回值
            result.code = json.optInt("code");
            // 调用标识符
            
              if(!json.isNull("cid")){
                  result.cid = json.optString("cid", null);
              }
            // 用作特定场景使用
            
              if(!json.isNull("data")){
                  result.data = json.optString("data", null);
              }
            // API调用状态，code的信息请参考ApiCode定义文件
            JSONArray stateListArray = json.optJSONArray("stateList");
            if (stateListArray != null) {
                int len = stateListArray.length();
                result.stateList = new ArrayList<Api_CallState>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = stateListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.stateList.add(Api_CallState.deserialize(jo));
                    }
                }
            }
      
            // 服务端返回的通知事件集合
            JSONArray notificationListArray = json.optJSONArray("notificationList");
            if (notificationListArray != null) {
                int len = notificationListArray.length();
                result.notificationList = new ArrayList<Api_KeyValuePair>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = notificationListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.notificationList.add(Api_KeyValuePair.deserialize(jo));
                    }
                }
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
        
        // 当前服务端时间
        json.put("systime", this.systime);
          
        // 调用返回值
        json.put("code", this.code);
          
        // 调用标识符
        if(this.cid != null) { json.put("cid", this.cid); }
          
        // 用作特定场景使用
        if(this.data != null) { json.put("data", this.data); }
          
        // API调用状态，code的信息请参考ApiCode定义文件 
        if (this.stateList != null) {
            JSONArray stateListArray = new JSONArray();
            for (Api_CallState value : this.stateList)
            {
                if (stateList != null) {
                    stateListArray.put(value.serialize());
                }
            }
            json.put("stateList", stateListArray);
        }
      
        // 服务端返回的通知事件集合 
        if (this.notificationList != null) {
            JSONArray notificationListArray = new JSONArray();
            for (Api_KeyValuePair value : this.notificationList)
            {
                if (notificationList != null) {
                    notificationListArray.put(value.serialize());
                }
            }
            json.put("notificationList", notificationListArray);
        }
      
        return json;
    }
}
  
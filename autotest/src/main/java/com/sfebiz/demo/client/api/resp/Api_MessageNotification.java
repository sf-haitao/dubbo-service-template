// Auto Generated.  DO NOT EDIT!
package com.sfebiz.demo.client.api.resp;
    
import org.json.JSONException;
import org.json.JSONObject;

public class Api_MessageNotification {

    /**
     * 消息内容
     */
    public String content;
      
    /**
     * 消息Id
     */
    public String msgId;
      
    /**
     * 消息类型0: 系统消息,1:通知消息,2: 聊天消息,3:群消息,4:留言消息,5:普通聊天 控制消息
     */
    public int type;
      
    /**
     * 消息内容类型
     */
    public int subType;
      
    /**
     * 发送方Id
     */
    public long fromUserId;
      
    /**
     * 接收方Id
     */
    public long toUserId;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_MessageNotification deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_MessageNotification deserialize(JSONObject json) throws JSONException {
        if (json != null && json.length() > 0) {
            Api_MessageNotification result = new Api_MessageNotification();
            
            // 消息内容
            
              if(!json.isNull("content")){
                  result.content = json.optString("content", null);
              }
            // 消息Id
            
              if(!json.isNull("msgId")){
                  result.msgId = json.optString("msgId", null);
              }
            // 消息类型0: 系统消息,1:通知消息,2: 聊天消息,3:群消息,4:留言消息,5:普通聊天 控制消息
            result.type = json.optInt("type");
            // 消息内容类型
            result.subType = json.optInt("subType");
            // 发送方Id
            result.fromUserId = json.optLong("fromUserId");
            // 接收方Id
            result.toUserId = json.optLong("toUserId");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 消息内容
        if(this.content != null) { json.put("content", this.content); }
          
        // 消息Id
        if(this.msgId != null) { json.put("msgId", this.msgId); }
          
        // 消息类型0: 系统消息,1:通知消息,2: 聊天消息,3:群消息,4:留言消息,5:普通聊天 控制消息
        json.put("type", this.type);
          
        // 消息内容类型
        json.put("subType", this.subType);
          
        // 发送方Id
        json.put("fromUserId", this.fromUserId);
          
        // 接收方Id
        json.put("toUserId", this.toUserId);
          
        return json;
    }
}
  
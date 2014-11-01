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
     * 1:文本消息，2:语音消息，3:图片消息，4:视频消息，10001 用户排队位置发生变更 或者医生排队人数变更,10002 医生引导用户退出就诊, 10003向用户发起转诊, 10004用户同意转诊, 10005专家把用户拉入诊室, 10006用户同意结束问诊, 10007用户拒绝结束问诊, 10008引导用户输入姓名, 10009引导用户输入年龄, 10010引导用户输入性别, 10015医生将用户拉进聊天室 自动返回欢迎语, 10018点评完成（选择好、中、差）, 10019转诊医生不在线消息, 11001活动消息 ,11002 习惯消息
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
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
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
            // 1:文本消息，2:语音消息，3:图片消息，4:视频消息，10001 用户排队位置发生变更 或者医生排队人数变更,10002 医生引导用户退出就诊, 10003向用户发起转诊, 10004用户同意转诊, 10005专家把用户拉入诊室, 10006用户同意结束问诊, 10007用户拒绝结束问诊, 10008引导用户输入姓名, 10009引导用户输入年龄, 10010引导用户输入性别, 10015医生将用户拉进聊天室 自动返回欢迎语, 10018点评完成（选择好、中、差）, 10019转诊医生不在线消息, 11001活动消息 ,11002 习惯消息
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
          
        // 1:文本消息，2:语音消息，3:图片消息，4:视频消息，10001 用户排队位置发生变更 或者医生排队人数变更,10002 医生引导用户退出就诊, 10003向用户发起转诊, 10004用户同意转诊, 10005专家把用户拉入诊室, 10006用户同意结束问诊, 10007用户拒绝结束问诊, 10008引导用户输入姓名, 10009引导用户输入年龄, 10010引导用户输入性别, 10015医生将用户拉进聊天室 自动返回欢迎语, 10018点评完成（选择好、中、差）, 10019转诊医生不在线消息, 11001活动消息 ,11002 习惯消息
        json.put("subType", this.subType);
          
        // 发送方Id
        json.put("fromUserId", this.fromUserId);
          
        // 接收方Id
        json.put("toUserId", this.toUserId);
          
        return json;
    }
}
  
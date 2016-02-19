package com.sfebiz.demo.client;

import com.sfebiz.demo.client.api.resp.Api_CreditNotification;
import com.sfebiz.demo.client.api.resp.Api_KeyValuePair;
import com.sfebiz.demo.client.api.resp.Api_MessageNotification;
import com.sfebiz.demo.client.logger.Logger;
import com.sfebiz.demo.client.logger.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guankaiqiang@ifunq.com
 *         返回结果中通用部分
 */
public class ServerResponse {
    private static final Logger logger = LoggerFactory.getLogger(ServerResponse.class);

    //通知中的积分关键字,与api-util.ConstField保持一致
    private static final String CREDIT_KEY = "CREDIT";
    //通知中的消息关键字,与api-util.ConstField保持一致
    private static final String MSG_KEY    = "MSG";

    //消息下发通知
    private List<Api_MessageNotification> messageNotifications;
    //积分下发通知
    private List<Api_CreditNotification>  creditNotifications;
    //返回时间
    private long   systime    = 0;
    //调用编号
    private String cid        = "";
    //返回结果编码
    private int    returnCode = 0;
    //特定场景使用
    private String data;

    public static boolean isNullOrEmpty(String value) {
        return value != null & value.length() > 0;
    }

    private boolean isCreditNotification(Api_KeyValuePair pair) {
        return isNullOrEmpty(pair.key) && isNullOrEmpty(pair.value) && pair.key.equalsIgnoreCase(CREDIT_KEY);
    }

    private boolean isMsgNotification(Api_KeyValuePair pair) {
        return isNullOrEmpty(pair.key) && isNullOrEmpty(pair.value) && pair.key.equalsIgnoreCase(MSG_KEY);
    }

    protected void setNotifications(List<Api_KeyValuePair> notifications) {
        try {
            if (notifications != null && notifications.size() > 0) {
                for (Api_KeyValuePair pair : notifications) {
                    if (isCreditNotification(pair)) {
                        JSONArray creditNotificationListArray = new JSONArray(pair.value);
                        if (creditNotificationListArray != null && creditNotificationListArray.length() > 0) {
                            int len = creditNotificationListArray.length();
                            creditNotifications = new ArrayList<Api_CreditNotification>(len);
                            for (int i = 0; i < len; i++) {
                                JSONObject jo = creditNotificationListArray.optJSONObject(i);
                                if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                                    creditNotifications.add(Api_CreditNotification.deserialize(jo));
                                }
                            }
                        }
                    } else if (isMsgNotification(pair)) {
                        JSONArray messageNotificationListArray = new JSONArray(pair.value);
                        if (messageNotificationListArray != null && messageNotificationListArray.length() > 0) {
                            int len = messageNotificationListArray.length();
                            messageNotifications = new ArrayList<Api_MessageNotification>(len);
                            for (int i = 0; i < len; i++) {
                                JSONObject jo = messageNotificationListArray.optJSONObject(i);
                                if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                                    messageNotifications.add(Api_MessageNotification.deserialize(jo));
                                }
                            }
                        }
                    }
                    //对该版本不支持的通知丢弃
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            logger.error(e, "deserialize notification failed.");
        }
    }
    /**
     * 获取结果返回时间
     *
     * @return resp.systime
     */
    public long getSystime() {
        return systime;
    }

    protected void setSystime(long systime) {
        this.systime = systime;
    }
    /**
     * 调用编号
     *
     * @return 调用编号
     */
    public String getCid() {
        return cid;
    }
    protected void setCid(String cid) {
        this.cid = cid;
    }

    /**
     * 获取消息通知列表
     *
     * @return 消息通知列表
     */
    public List<Api_MessageNotification> getMessageNotifications() {
        return messageNotifications;
    }
    /**
     * 获取积分通知列表
     *
     * @return 积分通知列表
     */
    public List<Api_CreditNotification> getCreditNotifications() {
        return creditNotifications;
    }

    /**
     * 获取错误码
     *
     * @return
     */
    public int getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * 获取错误数据
     *
     * @return
     */
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}

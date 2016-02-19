package com.sfebiz.demo.client;

import com.sfebiz.demo.client.logger.Logger;
import com.sfebiz.demo.client.logger.LoggerFactory;
import org.json.JSONObject;

import java.util.HashMap;

public abstract class BaseRequest<T> {
    protected static final Logger                  logger           = LoggerFactory.getLogger(BaseRequest.class);
    private                HashMap<String, String> verifyMsgs       = null;
    private                String                  methodName       = null;
    private                Runnable                responseListener = null;
    protected              ParameterList           params           = new ParameterList();
    protected              Response<T>             response         = new Response<T>();

    long systime      = 0;
    int  securityType = 0;

    public BaseRequest(String methodName, int securityType) {
        this.securityType = securityType;
        this.methodName = methodName;
        params.put(CommonParameter.method, methodName);
    }

    public final String getMethodName() {
        return methodName;
    }

    public final int getReturnCode() {
        return response.code;
    }

    public final String getReturnMessage() {
        return response.message;
    }

    public final long getSystime() {
        return systime;
    }

    public final int getSecurityType() {
        return securityType;
    }

    public final void putExt(String name, String value) {
        params.put(name, value);
    }

    public final void setBusinessId(String businessId) {
        params.put(CommonParameter.businessId, businessId);
    }

    /**
     * 用于记录访问日志
     *
     * @return
     */
    public final String getStringInfo() {
        if (params != null) {
            StringBuilder sb = new StringBuilder(params.size() * 10);
            for (String key : params.keySet()) {
                sb.append(key);
                sb.append("=");
                sb.append(params.get(key));
                sb.append("&");
            }
            return sb.toString();
        }
        return "";
    }

    abstract protected T getResult(JSONObject json);

    protected final void setVerifyError(String name, String msg) {
        if (verifyMsgs == null) {
            verifyMsgs = new HashMap<String, String>();
        }
        verifyMsgs.put(name, msg);
    }

    protected final void removeVerifyError(String name) {
        if (verifyMsgs != null) {
            verifyMsgs.remove(name);
        }
    }

    public final HashMap<String, String> getVerifyErrs() {
        return verifyMsgs;
    }

    final void fillResponse(int code, int length, String msg, JSONObject json) {
        response.code = code;
        response.length = length;
        response.message = msg;
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            response.result = getResult(json);
        }
    }

    /**
     * 为支持流式数据加载方式，当在一个http请求中发起多个api请求，且api请求返回值体积较大时，使用这种方式可以降低对内存的需求量
     *
     * @param listener
     */
    public void setResponseListener(Runnable listener) {
        responseListener = listener;
    }

    void responseLoaded() {
        if (responseListener != null) {
            responseListener.run();
        }
    }

    public final T getResponse() {
        return response.result;
    }
}
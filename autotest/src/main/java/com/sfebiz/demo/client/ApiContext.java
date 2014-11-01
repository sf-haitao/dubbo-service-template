package com.sfebiz.demo.client;

import com.sfebiz.demo.client.api.request.ApiCode;
import com.sfebiz.demo.client.api.resp.Api_CallState;
import com.sfebiz.demo.client.api.resp.Api_Response;
import com.sfebiz.demo.client.logger.Logger;
import com.sfebiz.demo.client.logger.LoggerFactory;
import com.sfebiz.demo.client.util.Base64Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiContext {
    private static final Logger logger       = LoggerFactory.getLogger(ApiContext.class);
    private static final Object signLocker   = new Object();
    private static       String rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDoIjY+VacM/v0q47oQkbE4eVo4AS/Px07EMCmlYmRjY9x1OeippSppQ1eNRIuFCbZRqpMoayDO68UdWPCSqOt1I8Uw03MzVDmy38ZBo6dVTRrqWW9z7vbQQ1nWkEcUWcRTIQIktQ2ptO4AOlZa1x1/zvsNBodTNqhqCGPeTNUwyQIDAQAB";

    private String appid           = null;
    private int    vercode         = 0;
    private String location        = null;
    private long   deviceId        = 0;
    private String deviceSecret    = null;
    private String deviceToken     = null;
    private long   userId          = 0;
    private String userToken       = null;
    private long   userTokenExpire = 0;
    private String phoneNumber     = null;
    private String dynamic         = null;

    public ApiContext(String appid, int vercode) {
        this.appid = appid;
        this.vercode = vercode;
    }

    public ApiContext(String appid, int vercode, String location) {
        this.appid = appid;
        this.vercode = vercode;
        this.location = location;
    }

    public static void setRsaPublicKey(String key) {
        rsaPublicKey = key;
    }

    public void setPhoneNumberAndDynamic(String phoneNumber, String dynamic) {
        this.phoneNumber = phoneNumber;
        this.dynamic = dynamic;
    }

    public void setDeviceInfo(long deviceId, String deviceSecret, String deviceToken) {
        this.deviceId = deviceId;
        this.deviceSecret = deviceSecret;
        this.deviceToken = deviceToken;
    }

    public void setUserInfo(long userId, String tk) {
        this.userId = userId;
        this.userToken = tk;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public String getAppId() {
        return appid;
    }

    public void setUserTokenExpire(long time) {
        userTokenExpire = time;
    }

    public long getUserTokenExpire() {
        return userTokenExpire;
    }

    public boolean hasDeviceInfo() {
        return deviceId != 0 && deviceSecret != null && deviceSecret.length() > 0 && deviceToken != null && deviceToken.length() > 0;
    }

    public String getParameterString(BaseRequest<?> request) {
        return getParameterString(new BaseRequest<?>[] { request });
    }

    public String getParameterString(BaseRequest<?>[] requests) {
        int securityType = 0;
        final int len = requests.length;
        ParameterList params = new ParameterList(len * 2);
        StringBuilder methodNames = new StringBuilder();

        for (int i = 0; i < len; i++) {
            BaseRequest<?> req = requests[i];

            securityType = securityType | req.securityType;
            for (String key : req.params.keySet()) {
                if (CommonParameter.method.equals(key)) {
                    methodNames.append(req.params.get(key));
                    methodNames.append(",");
                } else {
                    if (len == 1) {
                        params.put(key, req.params.get(key));
                    } else {
                        params.put(i + "_" + key, req.params.get(key));
                    }
                }
            }
        }
        if (methodNames.length() > 0) {
            methodNames.setLength(methodNames.length() - 1);
        }
        params.put(CommonParameter.method, methodNames.toString());

        params.put(CommonParameter.format, "json");
        if (location != null && !params.containsKey(CommonParameter.location)) {
            params.put(CommonParameter.location, location);
        }

        if (deviceId != 0 && !params.containsKey(CommonParameter.deviceId)) {
            params.put(CommonParameter.deviceId, "" + deviceId);
        }

        if (appid != null && !params.containsKey(CommonParameter.applicationId)) {
            params.put(CommonParameter.applicationId, appid);
        }

        if (userId > 0 && !params.containsKey(CommonParameter.userId)) {
            params.put(CommonParameter.userId, String.valueOf(userId));
        }

        if (vercode > 0 && !params.containsKey(CommonParameter.versionCode)) {
            params.put(CommonParameter.versionCode, "" + vercode);
        }

        return getParameterStringInternal(params, securityType);
    }

    public ServerResponse fillResponse(BaseRequest<?>[] requests, InputStream data) {
        ServerResponse commonResponse = new ServerResponse();
        int errorCode = 0;
        if (data != null) {
            try {
                InputStreamReader in = new InputStreamReader(data, "utf-8");
                StringBuilder sb = new StringBuilder();
                char[] cs = new char[2048];
                int size = 0;
                while ((size = in.read(cs)) >= 0) {
                    sb.append(cs, 0, size);
                }
                if (ApiConfig.isDebug) {
                    logger.info("responses: " + sb);
                }
                JSONObject json = new JSONObject(sb.toString());
                JSONArray content = json.getJSONArray("content");
                Api_Response resp = Api_Response.deserialize(json.getJSONObject("stat"));
                if (resp != null) {
                    commonResponse.setSystime(resp.systime);
                    commonResponse.setCid(resp.cid);
                    commonResponse.setNotifications(resp.notificationList);
                    commonResponse.setReturnCode(resp.code);
                    commonResponse.setData(resp.data);
                    if (resp.code == 0) {
                        if (resp.stateList != null && requests.length == resp.stateList.size()) {
                            List<Api_CallState> statList = resp.stateList;
                            int respSize = statList.size();
                            for (int i = 0; i < respSize; i++) {
                                Api_CallState state = statList.get(i);
                                BaseRequest<?> request = requests[i];
                                request.systime = resp.systime;
                                request.fillResponse(state.code, state.length, state.msg, content.getJSONObject(i));
                                request.responseLoaded();
                            }
                            return commonResponse;
                        } else {
                            errorCode = ApiCode.UNKNOWN_ERROR;
                        }
                    } else {
                        errorCode = resp.code;
                    }

                    if (errorCode != 0) {
                        for (BaseRequest<?> request : requests) {
                            request.systime = resp.systime;
                            request.fillResponse(errorCode, -1, "common error", null);
                            request.responseLoaded();
                        }
                        return commonResponse;
                    }
                } else {
                    logger.error("parse response error.");
                }
            } catch (SocketTimeoutException se) {
                logger.error(se, "socket timeout");
                throw new LocalException(LocalException.SOCKET_TIMEOUT);
            } catch (Exception e) {
                logger.error(e, "parse response error. cid=" + commonResponse.getCid());
            }
        }
        return commonResponse;
    }

    public void fillError(BaseRequest<?> request, int code) {
        request.fillResponse(code, 0, "", null);
    }

    public void fillError(BaseRequest<?>[] requests, int code) {
        int size = requests.length;
        for (int i = 0; i < size; i++) {
            requests[i].fillResponse(code, 0, "", null);
        }
    }

    private void signRequest(ParameterList params, int securityType) {
        if (ApiConfig.isDebug) {
            if (securityType == SecurityType.Internal || securityType == SecurityType.Integrate) {
                return;
            }
        }
        if (params.containsKey(CommonParameter.signature)) {
            return;
        }
        try {
            params.put(CommonParameter.signatureMethod, "sha1");
            MessageDigest sha = MessageDigest.getInstance("SHA1");
            StringBuilder sb = new StringBuilder(params.size() * 5);
            List<String> paramNames = new ArrayList<String>(params.keySet());
            Collections.sort(paramNames);
            for (String key : paramNames) {
                sb.append(key);
                sb.append('=');
                sb.append(params.get(key));
            }

            if (securityType == 0) {
                sb.append("sfhaitao.xyz!");
            } else {
                if (deviceSecret == null || deviceSecret.length() == 0) {
                    throw new RuntimeException("device secret is null.");
                }
                sb.append(deviceSecret);

            }
            if (ApiConfig.isDebug) {
                logger.info("before sig:" + sb.toString());
            }
            params.put(CommonParameter.signature,
                    new String(Base64Util.encode(sha.digest(sb.toString().getBytes("utf-8")), Base64Util.NO_WRAP), "utf-8"));
        } catch (Exception e) {
            throw new RuntimeException("sign url failed.", e);
        }
    }

    private String getParameterStringInternal(ParameterList params, int securityType) {
        if (userToken != null) {
            if (!params.containsKey(CommonParameter.token)) {
                params.put(CommonParameter.token, userToken);
            }
        } else {
            if (((securityType & SecurityType.UserLogin) > 0) | ((securityType & SecurityType.UserTrustedDevice) > 0)) {
                throw new LocalException(LocalException.TOKEN_MISSING);
            }
            if (ApiConfig.isDebug) {
                if (securityType > 0) {
                    if (deviceSecret != null) {
                        if (!params.containsKey(CommonParameter.deviceToken)) {
                            params.put(CommonParameter.deviceToken, deviceToken);
                        }
                    } else if (securityType == SecurityType.Internal || securityType == SecurityType.Integrate) {
                        // do nothing.
                    } else {
                        throw new LocalException(LocalException.TOKEN_MISSING);
                    }
                }
            } else {
                if (securityType > 0) {
                    if (deviceSecret != null) {
                        if (!params.containsKey(CommonParameter.deviceToken)) {
                            params.put(CommonParameter.deviceToken, deviceToken);
                        }
                    } else {
                        throw new LocalException(LocalException.TOKEN_MISSING);
                    }
                }
            }
        }

        if (((securityType & SecurityType.MobileOwner) > 0) | ((securityType & SecurityType.MobileOwnerTrustedDevice) > 0)) {
            params.put(CommonParameter.phoneNumber, phoneNumber);
            params.put(CommonParameter.dynamic, dynamic);
        }

        signRequest(params, securityType);

        if (params.size() > 0) {
            try {
                StringBuilder sb = new StringBuilder(params.size() * 7);
                for (String key : params.keySet()) {
                    sb.append(key);
                    sb.append('=');
                    sb.append(URLEncoder.encode(params.get(key), "utf-8"));
                    sb.append('&');
                }
                sb.setLength(sb.length() - 1);
                return sb.toString();
            } catch (Exception e) {
                throw new RuntimeException("invalid request", e);
            }
        }
        throw new RuntimeException("invalid request");
    }
}

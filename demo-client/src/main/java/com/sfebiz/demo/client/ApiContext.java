package com.sfebiz.demo.client;

import com.sfebiz.demo.client.api.request.ApiCode;
import com.sfebiz.demo.client.api.resp.Api_CallState;
import com.sfebiz.demo.client.api.resp.Api_Response;
import com.sfebiz.demo.client.logger.Logger;
import com.sfebiz.demo.client.logger.LoggerFactory;
import com.sfebiz.demo.client.util.Base64Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiContext {
    private static final Logger logger     = LoggerFactory.getLogger(ApiContext.class);
    private static final Object signLocker = new Object();

    private X509Certificate certificate     = null;
    private PrivateKey      pk              = null;
    private String          token           = null;
    private String          deviceId        = null;
    private String          appid           = null;
    private String          location        = null;
    private long            userId          = 0;
    private String          deviceInfo      = null;
    private int             vercode         = 0;
    private long            tokenExpireTime = 0;
    private String          deviceToken     = null;
    private String          phoneNumber     = null;
    private String          dynamic         = null;
    private static String rsaPubKey;

    /**
     * @param appid
     * @param vercode
     *
     * @see com.pingan.jk.client.ApiContext 请设置rsapublickey
     * @deprecated
     */
    @Deprecated
    public ApiContext(String appid, int vercode) {
        this.appid = appid;
        this.vercode = vercode;
    }

    public ApiContext(String appid, int vercode, String rsaPublicKey) {
        this.appid = appid;
        this.vercode = vercode;
        rsaPubKey = rsaPublicKey;
    }

    public static void setRsaPubKey(String rsaPublicKey) {
        rsaPubKey = rsaPublicKey;
    }

    /**
     * 获取用来进行消息加密的rsa公钥
     *
     * @return
     */
    public static String getRsaPubKey() {
        if (rsaPubKey != null && !rsaPubKey.isEmpty()) {
            return rsaPubKey;
        } else {
            throw new RuntimeException("rsa public key is miss");
        }
    }
    public void setCertificateWithDeviceToken(String fileName, String dtk) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(fileName), "110110".toCharArray());
            X509Certificate cert = (X509Certificate)ks.getCertificate(ks.aliases().nextElement());
            PrivateKey pk = (PrivateKey)ks.getKey(ks.aliases().nextElement(), "110110".toCharArray());
            setCertificate(cert, pk);
            deviceToken = dtk;
        } catch (Exception e) {
            throw new LocalException(e, "cert error.", LocalException.CERT_BROKEN);
        }
    }

    public void setCertificateWithDeviceToken(InputStream fis, String dtk) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(fis, "110110".toCharArray());
            X509Certificate cert = (X509Certificate)ks.getCertificate(ks.aliases().nextElement());
            PrivateKey pk = (PrivateKey)ks.getKey(ks.aliases().nextElement(), "110110".toCharArray());
            setCertificate(cert, pk);
            deviceToken = dtk;
        } catch (Exception e) {
            throw new LocalException(e, "cert error.", LocalException.CERT_BROKEN);
        }
    }

    public void setPhoneNumberAndDynamic(String phoneNumber, String dynamic) {
        this.phoneNumber = phoneNumber;
        this.dynamic = dynamic;
    }

    public void setCertificateWithDeviceToken(byte[] keystore, String dtk) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new ByteArrayInputStream(keystore), "110110".toCharArray());
            X509Certificate cert = (X509Certificate)ks.getCertificate(ks.aliases().nextElement());
            PrivateKey pk = (PrivateKey)ks.getKey(ks.aliases().nextElement(), "110110".toCharArray());
            setCertificate(cert, pk);
            deviceToken = dtk;
        } catch (Exception e) {
            throw new LocalException(e, "cert error.", LocalException.CERT_BROKEN);
        }
    }

    private synchronized void setCertificate(X509Certificate cert, PrivateKey privateKey) {
        certificate = cert;
        pk = privateKey;

        String subject = certificate.getSubjectDN().getName();
        int cnStart = subject.indexOf("CN=");
        int cnEnd = subject.indexOf(",", cnStart);
        String cn = null;
        if (cnEnd != -1) {
            cn = subject.substring(cnStart + 3, cnEnd);
        } else {
            cn = subject.substring(cnStart + 3);
        }
        deviceId = cn;
        int diStart = subject.indexOf("1.2.4.14.4.8.7.21.2=");
        int diEnd = subject.indexOf(",", diStart);
        if (diEnd != -1) {
            deviceInfo = subject.substring(diStart + 20, diEnd);
        } else {
            deviceInfo = subject.substring(diStart + 20);
        }
        int appidStart = subject.indexOf("1.2.4.14.4.8.7.21.1=");
        int appidEnd = subject.indexOf(",", appidStart);
        //不适用证书中的appid参与业务
        //        if (appidEnd != -1) {
        //            appid = subject.substring(appidStart + 20, appidEnd);
        //        } else {
        //            appid = subject.substring(appidStart + 20);
        //        }
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setToken(String tk) {
        token = tk;
    }

    public String getToken() {
        return token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long id) {
        userId = id;
    }

    public String getAppId() {
        return appid;
    }

    public void setTokenExpireTime(long time) {
        tokenExpireTime = time;
    }

    public long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public boolean hasCertificate() {
        return pk != null;
    }

    public String getParameterString(BaseRequest<?> request) {
        return getParameterString(new BaseRequest<?>[]{request});
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

        if (deviceId != null && !params.containsKey(CommonParameter.deviceId)) {
            params.put(CommonParameter.deviceId, deviceId);
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
                    //modify by xuedong,guankaiqiang
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
        //serverResponse中将错误码以及错误信息暴露
        //        //通过LocalException将异常抛出
        //        if (errorCode != 0) {
        //            LocalException errorCodeException = new LocalException(errorCode);
        //            throw errorCodeException;
        //        }
        return commonResponse;
    }

    public String getCertEncoded() {
        if (certificate == null) {
            throw new RuntimeException("certificate is null.");
        }
        try {
            return Base64Util.encodeToString(certificate.getEncoded());
        } catch (CertificateEncodingException e) {
            throw new RuntimeException("certificate error.", e);
        }
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
        if (params.containsKey(CommonParameter.signature)) return;
        try {
            if (pk != null && securityType > 0) {
                if ("EC".equalsIgnoreCase(pk.getAlgorithm())) {
                    if (!params.containsKey(CommonParameter.signatureMethod)) {
                        params.put(CommonParameter.signatureMethod, "ecc");
                    }
                } else if ("RSA".equalsIgnoreCase(pk.getAlgorithm())) {
                    if (!params.containsKey(CommonParameter.signatureMethod)) {
                        params.put(CommonParameter.signatureMethod, "rsa");
                    }
                } else {
                    if (!params.containsKey(CommonParameter.signatureMethod)) {
                        params.put(CommonParameter.signatureMethod, "ecc");
                    }
                }
            }
            StringBuilder sb = new StringBuilder(params.size() * 5);
            List<String> paramNames = new ArrayList<String>(params.keySet());
            Collections.sort(paramNames);
            for (String key : paramNames) {
                sb.append(key);
                sb.append('=');
                sb.append(params.get(key));
            }

            if (securityType == 0) {
                sb.append("jk.pingan.com");
                if (ApiConfig.isDebug) {
                    logger.info("before sig:" + sb.toString());
                }
                MessageDigest sha = MessageDigest.getInstance("SHA1");
                params.put(CommonParameter.signature,
                           new String(Base64Util.encode(sha.digest(sb.toString().getBytes("utf-8")), Base64Util.NO_WRAP), "utf-8"));
            } else {
                if (certificate == null || pk == null) {
                    throw new RuntimeException("certificate is null.");
                }
                if (ApiConfig.isDebug) {
                    logger.info("before sig:" + sb.toString());
                }
                byte[] bs = sb.toString().getBytes("utf-8");
                Signature sig = null;
                if ("EC".equalsIgnoreCase(pk.getAlgorithm())) {
                    sig = Signature.getInstance("SHA1withECDSA");
                } else if ("RSA".equalsIgnoreCase(pk.getAlgorithm())) {
                    sig = Signature.getInstance("SHA1withRSA");
                } else {
                    sig = Signature.getInstance("SHA1withECDSA");
                }
                byte[] s = null;
                // 对签名部分进行同步
                synchronized (signLocker) {
                    sig.initSign(pk);
                    sig.update(bs);
                    s = sig.sign();
                }
                String signature = new String(Base64Util.encode(s, Base64Util.NO_WRAP), "utf-8");
                params.put(CommonParameter.signature, signature);
            }
        } catch (Exception e) {
            throw new RuntimeException("sign url failed.", e);
        }
    }

    private String getParameterStringInternal(ParameterList params, int securityType) {
        if (token != null) {
            if (!params.containsKey(CommonParameter.token)) {
                params.put(CommonParameter.token, token);
            }
        } else {
            if (((securityType & SecurityType.UserLogin) > 0) | ((securityType & SecurityType.UserTrustedDevice) > 0)) {
                throw new LocalException(LocalException.TOKEN_MISSING);
            }
            if (securityType > 0) {
                if (deviceToken != null) {
                    if (!params.containsKey(CommonParameter.deviceToken)) {
                        params.put(CommonParameter.deviceToken, deviceToken);
                    }
                } else {
                    throw new LocalException(LocalException.TOKEN_MISSING);
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

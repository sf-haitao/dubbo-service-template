package com.sfebiz.demo.autotest.test;

import java.io.IOException;
import java.io.InputStream;

import com.sfebiz.demo.client.ServerResponse;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class WebRequestUtil {
    private static final int MAX_CONNECTION_SIZE        = 200;
    private static final int SOCKET_TIMEOUT             = 30000;
    private static final int CONNECTION_TIMEOUT         = 3000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 30000;

    private static CloseableHttpClient hc = null;
    private static RequestConfig       rc = null;

    synchronized private static CloseableHttpClient getHttpClient() {
        if (hc == null) {
            if (hc == null) {
                PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
                cm.setMaxTotal(MAX_CONNECTION_SIZE);
                cm.setDefaultMaxPerRoute(20);
                cm.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(Consts.UTF_8).build());
                hc = HttpClients.custom().setConnectionManager(cm).setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {

                    public long getKeepAliveDuration(HttpResponse arg0, org.apache.http.protocol.HttpContext arg1) {
                        return 30000;
                    }
                }).build();
                rc = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(
                        CONNECTION_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setExpectContinueEnabled(
                        false).setRedirectsEnabled(false).build();
            }
        }
        return hc;
    }

    public static String getResponseString(String baseUrl, String params, boolean useGzip) {
        CloseableHttpResponse resp = null;
        try {
            resp = getHttpResponse(baseUrl, params, "", useGzip);
            String result = EntityUtils.toString(resp.getEntity(), "utf-8");
            return result;
        } catch (Exception e) {
            throw new RuntimeException(baseUrl + " " + params, e);
        } finally {
            if (resp != null) {
                try {
                    resp.close();
                } catch (Exception e) {
                    throw new RuntimeException(baseUrl + " " + params, e);
                }
            }
        }
    }

    public static ServerResponse fillResponse(String baseUrl, String params, String cid, boolean useGzip, ResponseFiller f) {
        CloseableHttpResponse resp = null;
        InputStream is = null;
        ServerResponse serverResponse = null;
        try {
            resp = getHttpResponse(baseUrl, params, cid, useGzip);
            is = resp.getEntity().getContent();
            serverResponse = f.fill(is);
        } catch (Exception e) {
            throw new RuntimeException(cid + ",1 " + baseUrl + "?" + params, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    throw new RuntimeException(cid + ",2 " + baseUrl + "?" + params, e);
                }
            }
            if (resp != null) {
                try {
                    resp.close();
                } catch (Exception e) {
                    throw new RuntimeException(cid + ",3 " + baseUrl + "?" + params, e);
                }
            }
        }
        return serverResponse;
    }

    private static CloseableHttpResponse getHttpResponse(String baseUrl, String params, String cid,
                                                         boolean useGzip) throws ClientProtocolException, IOException {
        CloseableHttpClient client = getHttpClient();
        HttpRequestBase req = null;
        if (params == null) {
            req = new HttpGet(baseUrl);
        } else if (params.length() > 200) {
            System.out.println("Post请求:" + params);
            HttpPost post = new HttpPost(baseUrl);
            StringEntity se = new StringEntity(params, "utf-8");
            se.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
            post.setEntity(se);
            req = post;
        } else {
            System.out.println("Get请求:" + baseUrl + "?" + params);
            req = new HttpGet(baseUrl + "?" + params);
        }
        req.setConfig(rc);
        if (useGzip) {
            req.setHeader("Accept-Encoding", "gzip");
        }
        CloseableHttpResponse resp = null;
        resp = client.execute(req);
        int statusCode = resp.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new RuntimeException(cid + ",0 " + baseUrl + "?" + params + " code:" + statusCode);
        }
        return resp;
    }

    public static interface ResponseFiller {
        ServerResponse fill(InputStream is);
    }

    /**
     * 用于直接将xml格式的数据post过去
     *
     * @param url
     * @param xmlEntity : xml的字符串
     *
     * @return
     */
    public static String httpPost(String url, String xmlEntity) {
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new StringEntity(xmlEntity));

            HttpResponse httpResponse = new DefaultHttpClient().execute(post);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // 使用getEntity方法获得返回结果
                String result = EntityUtils.toString(httpResponse.getEntity());
                return result;
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return "";
    }
}

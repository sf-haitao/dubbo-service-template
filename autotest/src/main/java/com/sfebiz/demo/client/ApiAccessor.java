package com.sfebiz.demo.client;

import android.os.Looper;
import com.sfebiz.demo.client.api.request.ApiCode;
import com.sfebiz.demo.client.logger.Logger;
import com.sfebiz.demo.client.logger.LoggerFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class ApiAccessor {
    public static final Logger                         logger = LoggerFactory.getLogger(ApiAccessor.class);
    private static      ThreadLocal<DefaultHttpClient> client = new ThreadLocal<DefaultHttpClient>();
    private ApiContext apiContext;
    private int        connTimeout;
    private int        soTimeout;
    private String     agent;
    private int        keepalive;
    private String     apiUrl;

    public ApiAccessor(ApiContext context, int connTimeout, int soTimeout, String agent, int keepalive, String apiUrl) {
        this.apiContext = context;
        this.connTimeout = connTimeout;
        this.soTimeout = soTimeout;
        this.agent = agent;
        this.keepalive = keepalive;
        this.apiUrl = apiUrl;
    }

    private DefaultHttpClient getHttpClient() {
        DefaultHttpClient hClient = client.get();
        if (hClient == null) {
            hClient = new DefaultHttpClient();
            HttpParams p = hClient.getParams();
            p.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connTimeout);
            p.setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout);
            p.setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
            p.setParameter(CoreProtocolPNames.USER_AGENT, agent);
            hClient.setCookieStore(null);
            if (keepalive > 0) {
                hClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {

                    @Override
                    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                        return keepalive;
                    }
                });
            }
            client.set(hClient);
        }
        return hClient;
    }

    /**
     * 非主线程请求Api接口进行单个请求时调用
     *
     * @param request 要执行的Api请求
     */
    public ServerResponse fillApiResponse(BaseRequest<?> request) {
        return fillApiResponse(new BaseRequest<?>[]{request});
    }

    /**
     * 非主线程请求Api接口进行组合请求时调用
     *
     * @param requests 要执行的Api请求集合
     */
    public ServerResponse fillApiResponse(BaseRequest<?>[] requests) {
        ServerResponse commonResponse = null;
        if (ApiConfig.isDebug) {
            if (Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper()) {
                throw new RuntimeException("不要在主线程进行api调用");
            }
        }

        if (requests == null || requests.length == 0) {
            return commonResponse;
        }
        DefaultHttpClient hClient = getHttpClient();

        String params = apiContext.getParameterString(requests);
        InputStream input = null;
        try {
            String cid = "" + (long)(Math.random() * 10000000000L);
            HttpResponse response = null;
            if (params.length() > 200) {
                HttpPost post = new HttpPost(ApiConfig.apiUrl);
                if (keepalive == 0) {
                    post.setHeader("Connection", "close");
                }
                post.setHeader("cid", cid);
                if (ApiConfig.useHttpGzip) {
                    post.setHeader("Accept-Encoding", "gzip");
                }
                StringEntity se = new StringEntity(params, "utf-8");
                se.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
                post.setEntity(se);
                response = hClient.execute(post);
            } else {
                String url = apiUrl + "?" + params;
                HttpGet get = new HttpGet(url);
                if (keepalive == 0) {
                    get.setHeader("Connection", "close");
                }
                get.setHeader("cid", cid);
                if (ApiConfig.useHttpGzip) {
                    get.setHeader("Accept-Encoding", "gzip");
                }
                response = hClient.execute(get);
            }

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                String url = ApiConfig.apiUrl + "?" + params;
                logger.error("Api access failed. httpcode:" + statusCode + "  url=" + url);
                throw new LocalException(ApiCode.ACCESS_DENIED);
            }

            HttpEntity httpEntity = response.getEntity();
            input = httpEntity.getContent();
            Header contentType = httpEntity.getContentEncoding();
            if (contentType != null) {
                String value = contentType.getValue();
                if (value != null && value.contains("gzip")) {
                    input = new GZIPInputStream(input);
                }
            }

            if (ApiConfig.isDebug) {
                String url = ApiConfig.apiUrl + "?" + params;
                logger.info("request to :" + url + "  content length=" + httpEntity.getContentLength());
            }
            commonResponse = apiContext.fillResponse(requests, input);
        } catch (LocalException ae) {
            //            apiContext.fillError(requests, ae.getCode());
            LocalException localException = new LocalException(ae, ae.getMessage(), ae.getCode());
            localException.setErrorData(ae.getErrorData());
            throw localException;
        } catch (Throwable t) {
            client.set(null);
            hClient.getConnectionManager().shutdown();
            logger.error(t, "Api access failed.");
            throw new LocalException(LocalException.UNKNOWN);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {/* do nothing */}
            }
        }
        return commonResponse;
    }
}

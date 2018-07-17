package com.adpanshi.cashloan.common.http.client;

import com.adpanshi.cashloan.common.http.HttpHandle;
import com.adpanshi.cashloan.common.http.HttpResult;
import com.adpanshi.cashloan.common.http.ParameterConverter;
import com.alibaba.fastjson.JSON;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by zsw on 2018/6/23 0023.
 */
public class HttpClientHandle implements HttpHandle
{
    private final Logger logger = LoggerFactory.getLogger(HttpClientHandle.class);

    public HttpResult sendSSLGetRequest(String url, Map<String, String> textParams)
    {
        return sendSSLGetRequest(url, null, textParams, null);
    }

    public HttpResult sendSSLGetRequest(String url, Map<String, String> requestHeaders, Map<String, String> textParams, String decodeCharset)
    {
        return sendGetRequest(url, requestHeaders, textParams, decodeCharset, true, true);
    }

    public HttpResult sendGetRequest(String url, Map<String, String> textParams)
    {
        return sendGetRequest(url, null, textParams, null, false, true);
    }

    public HttpResult sendGetRequest(String url, Map<String, String> requestHeaders, Map<String, String> textParams, String decodeCharset, boolean isSSL, boolean isNeedURLEncode)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        int code = 0;
        try {
            if (isSSL) {
                setSSL(httpClient);
            }
            String query = ParameterConverter.convert2URL(textParams, isNeedURLEncode);
            StringBuffer path = new StringBuffer();
            path.append(url);
            if ((query != null) && (query.length() > 0)) {
                path.append("?" + query);
            }
            HttpGet httpGet = new HttpGet(path.toString());
            setRequestHeader(requestHeaders, httpGet);
            HttpResponse response = httpClient.execute(httpGet);
            code = response.getStatusLine().getStatusCode();

            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            this.logger.error("与" + url + "通讯时发生异常", e);
        }
        return new HttpResult(Integer.valueOf(code), responseContent);
    }

    public HttpResult sendSSLPostRequest(String url, Map<String, String> bodyParams)
    {
        return sendSSLPostRequest(url, null, bodyParams, null);
    }

    public HttpResult sendSSLPostRequest(String url, Map<String, String> requestHeaders, Map<String, String> bodyParams, String decodeCharset)
    {
        return sendPostRequest(url, requestHeaders, bodyParams, decodeCharset, true);
    }

    public HttpResult sendPostRequest(String url, Map<String, String> bodyParams)
    {
        return sendPostRequest(url, null, bodyParams, null, false);
    }

    public HttpResult sendPostRequest(String url, Map<String, String> requestHeaders, Map<String, String> bodyParams, String decodeCharset, boolean isSSL)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        int code = 0;
        try {
            if (isSSL) {
                setSSL(httpClient);
            }
            HttpPost post = new HttpPost(url);
            setRequestHeader(requestHeaders, post);
            List valuePairs = ParameterConverter.convert2ValuePair(bodyParams);
            if ((valuePairs != null) && (valuePairs.size() > 0)) {
                post.setEntity(new UrlEncodedFormEntity(valuePairs, StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset));
            }
            HttpResponse response = httpClient.execute(post);
            code = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            this.logger.error("与" + url + "通讯时发生异常", e);
        }
        return new HttpResult(Integer.valueOf(code), responseContent);
    }

    public HttpResult sendSSLPostRequestWithXml(String url, String paramXmlStr)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        int code = 0;
        HttpPost post = new HttpPost(url);
        try {
            setSSL(httpClient);

            StringEntity postEntity = new StringEntity(paramXmlStr, "UTF-8");
            post.addHeader("Content-Type", "text/xml");
            post.setEntity(postEntity);
            HttpResponse response = httpClient.execute(post);
            code = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            this.logger.error("与" + url + "通讯时发生异常", e);
        }
        return new HttpResult(Integer.valueOf(code), responseContent);
    }

    public HttpResult sendSSLPostRequestWithJSON(String url, Object paramObj)
    {
        return sendSSLPostRequestWithJSON(url, null, paramObj, null);
    }

    public HttpResult sendSSLPostRequestWithJSON(String url, Map<String, String> requestHeaders, Object paramObj, String decodeCharset)
    {
        return sendPostRequestWithJSON(url, requestHeaders, paramObj, decodeCharset, true);
    }

    public HttpResult sendPostRequestWithJSON(String url, Object paramObj)
    {
        return sendPostRequestWithJSON(url, paramObj, 10000, 10000);
    }

    public HttpResult sendPostRequestWithJSON(String url, Object paramObj, int connectionTimeout, int sendDataTimeout)
    {
        return sendPostRequestWithJSON(url, null, paramObj, null, false, connectionTimeout, sendDataTimeout);
    }

    public HttpResult sendPostRequestWithJSON(String url, Map<String, String> requestHeaders, Object paramObj, String decodeCharset, boolean isSSL)
    {
        return sendPostRequestWithJSON(url, requestHeaders, paramObj, decodeCharset, isSSL, 10000, 10000);
    }

    public HttpResult sendPostRequestWithJSON(String url, Map<String, String> requestHeaders, Object paramObj, String decodeCharset, boolean isSSL, int connectionTimeout, int sendDataTimeout)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(connectionTimeout));
        httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(sendDataTimeout));
        int code = 0;
        try {
            if (isSSL) {
                setSSL(httpClient);
            }
            HttpPost post = new HttpPost(url);
            setRequestHeader(requestHeaders, post);
            if (paramObj != null) {
                post.setEntity(paramObject2Json(paramObj, decodeCharset));
            }
            HttpResponse response = httpClient.execute(post);
            code = response.getStatusLine().getStatusCode();

            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            this.logger.error("与" + url + "通讯时发生异常", e);
        }
        return new HttpResult(Integer.valueOf(code), responseContent);
    }

    public HttpResult sendSSLPutRequest(String url, Map<String, String> bodyParams) {
        return sendSSLPutRequest(url, null, bodyParams, null);
    }

    public HttpResult sendSSLPutRequest(String url, Map<String, String> requestHeaders, Map<String, String> bodyParams, String decodeCharset) {
        return sendPutRequest(url, requestHeaders, bodyParams, decodeCharset, true);
    }

    public HttpResult sendPutRequest(String url, Map<String, String> bodyParams) {
        return sendPutRequest(url, null, bodyParams, null, false);
    }

    public HttpResult sendPutRequest(String url, Map<String, String> requestHeaders, Map<String, String> bodyParams, String decodeCharset, boolean isSSL) {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        int code = 0;
        try {
            if (isSSL) {
                setSSL(httpClient);
            }
            HttpPut put = new HttpPut(url);
            setRequestHeader(requestHeaders, put);
            List valuePairs = ParameterConverter.convert2ValuePair(bodyParams);
            if ((valuePairs != null) && (valuePairs.size() > 0)) {
                put.setEntity(new UrlEncodedFormEntity(valuePairs, StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset));
            }
            HttpResponse response = httpClient.execute(put);
            code = response.getStatusLine().getStatusCode();

            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            this.logger.error("与" + url + "通讯时发生异常", e);
        }
        return new HttpResult(Integer.valueOf(code), responseContent);
    }

    public HttpResult sendSSLPutRequestWithJSON(String url, Object paramObj)
    {
        return sendSSLPutRequestWithJSON(url, null, paramObj, null);
    }

    public HttpResult sendSSLPutRequestWithJSON(String url, Map<String, String> requestHeaders, Object paramObj, String decodeCharset)
    {
        return sendPutRequestWithJSON(url, requestHeaders, paramObj, decodeCharset, true);
    }

    public HttpResult sendPutRequestWithJSON(String url, Object paramObj)
    {
        return sendPutRequestWithJSON(url, null, paramObj, null, false);
    }

    public HttpResult sendPutRequestWithJSON(String url, Map<String, String> requestHeaders, Object paramObj, String decodeCharset, boolean isSSL)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        int code = 0;
        try {
            if (isSSL) {
                setSSL(httpClient);
            }
            HttpPut put = new HttpPut(url);
            setRequestHeader(requestHeaders, put);
            if (paramObj != null) {
                put.setEntity(paramObject2Json(paramObj, decodeCharset));
            }
            HttpResponse response = httpClient.execute(put);
            code = response.getStatusLine().getStatusCode();

            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            this.logger.error("与" + url + "通讯时发生异常", e);
        }
        return new HttpResult(Integer.valueOf(code), responseContent);
    }

    private void setSSL(HttpClient httpClient) throws NoSuchAlgorithmException, KeyManagementException
    {
        X509TrustManager x509TrustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { x509TrustManager }, null);
        SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext);
        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
    }

    private void setRequestHeader(Map<String, String> requestHeaders, HttpRequest request) {
        if ((requestHeaders != null) && (requestHeaders.size() > 0))
            for (Map.Entry entry : requestHeaders.entrySet())
                request.addHeader((String)entry.getKey(), (String)entry.getValue());
    }

    private StringEntity paramObject2Json(Object paramObj, String decodeCharset)
    {
        Object jsonObj = JSON.toJSON(paramObj);
        StringEntity stringEntity = null;
        stringEntity = new StringEntity(jsonObj.toString(), StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset);
        stringEntity.setContentEncoding(StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset);
        stringEntity.setContentType("application/json");
        return stringEntity;
    }
}

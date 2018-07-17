package com.adpanshi.cashloan.common.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Created by zsw on 2018/6/23 0023.
 */
public class HttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String sendGetRequest(String reqURL)
    {
        return sendGetRequest(reqURL, "UTF-8");
    }

    public static String sendGetRequest(String reqURL, String decodeCharset)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.handle-redirects", false);
        HttpGet httpGet = new HttpGet(reqURL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity)
            {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下", e);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendGetRequest(String reqURL, Map<String, String> headerParams)
    {
        return sendGetRequest(reqURL, headerParams, "UTF-8");
    }

    public static String sendGetRequest(String reqURL, Map<String, String> headerParams, String decodeCharset)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.handle-redirects", false);
        HttpGet httpGet = new HttpGet(reqURL);
        try {
            if ((headerParams != null) && (headerParams.size() > 0)) {
                for (Map.Entry entry : headerParams.entrySet()) {
                    httpGet.setHeader((String)entry.getKey(), (String)entry.getValue());
                }
            }
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下", e);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendPostRequest(String reqURL, String sendData, boolean isEncoder)
    {
        return sendPostRequest(reqURL, sendData, isEncoder, null, null);
    }

    public static String sendPostRequest(String reqURL, String sendData, boolean isEncoder, String encodeCharset, String decodeCharset)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(reqURL);

        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        try {
            if (isEncoder) {
                List formParams = new ArrayList();
                for (String str : sendData.split("&")) {
                    formParams.add(new BasicNameValuePair(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=") + 1)));
                }
                httpPost.setEntity(new StringEntity(URLEncodedUtils.format(formParams, encodeCharset == null ? "UTF-8" : encodeCharset)));
            } else {
                httpPost.setEntity(new StringEntity(sendData));
            }

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(new StringBuilder().append("与[").append(reqURL).append("]通信过程中发生异常,堆栈信息如下").toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendPostRequest(String reqURL, Map<String, String> params, String encodeCharset, String decodeCharset)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();

        System.out.println(reqURL);
        HttpPost httpPost = new HttpPost(reqURL);
        List formParams = new ArrayList();
        for (Map.Entry entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair((String)entry.getKey(), String.valueOf(entry.getValue())));
            System.out.println(new StringBuilder().append((String)entry.getKey()).append("------------").append(String.valueOf(entry.getValue())).toString());
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset == null ? "UTF-8" : encodeCharset));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(new StringBuilder().append("与[").append(reqURL).append("]通信过程中发生异常,堆栈信息如下").toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendPostRequestForFiles(String reqURL, Map<String, String> params, Map<String, String> headerParams, String encodeCharset, String decodeCharset, Map<String, List<File>> fileMap)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        LOGGER.info(reqURL);
        try {
            HttpPost httpPost = new HttpPost(reqURL);
            Iterator localIterator1;
            if ((headerParams != null) && (headerParams.size() > 0))
                for (localIterator1 = headerParams.entrySet().iterator(); localIterator1.hasNext(); ) {
                    Map.Entry entry = (Map.Entry)localIterator1.next();
                    httpPost.setHeader((String)entry.getKey(), (String)entry.getValue());
                }
            Map.Entry entry;
            MultipartEntity multipartEntity = new MultipartEntity();
            if ((params != null) && (!params.isEmpty())) {
                for (Map.Entry paramEntry : params.entrySet()) {
                    multipartEntity.addPart((String)paramEntry.getKey(), new StringBody(paramEntry.getValue() == null ? "" : (String)paramEntry.getValue(), Charset.forName("UTF-8")));
                    System.out.println(new StringBuilder().append((String)paramEntry.getKey()).append("------------").append(String.valueOf(paramEntry.getValue())).toString());
                }
            }

            if ((fileMap != null) && (!fileMap.isEmpty()))
                for (Iterator fileEntry = fileMap.keySet().iterator(); fileEntry.hasNext(); ) {
                    String s = (String)fileEntry.next();
                    List entrys = (List)fileMap.get(s);
                    if ((entrys != null) && (entrys.size() > 0))
                        for (Object fileEntry1 : entrys)
                            multipartEntity.addPart(s, new FileBody((File)fileEntry1));
                }
            String s;
            httpPost.setEntity(multipartEntity);

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(new StringBuilder().append("与[").append(reqURL).append("]通信过程中发生异常,堆栈信息如下").toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendPostRequest(String reqURL, Map<String, String> params, Map<String, String> headerParams, String encodeCharset, String decodeCharset, Map<String, File> fileMap)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        LOGGER.info(reqURL);
        try {
            HttpPost httpPost = new HttpPost(reqURL);
            Iterator localIterator;
            if ((headerParams != null) && (headerParams.size() > 0))
                for (localIterator = headerParams.entrySet().iterator(); localIterator.hasNext(); ) {
                    Map.Entry entry = (Map.Entry)localIterator.next();
                    httpPost.setHeader((String)entry.getKey(), (String)entry.getValue());
                }
            Map.Entry entry;
            MultipartEntity multipartEntity = new MultipartEntity();
            if ((params != null) && (!params.isEmpty())) {
                for (Map.Entry paramEntry : params.entrySet()) {
                    multipartEntity.addPart((String)paramEntry.getKey(), new StringBody(paramEntry.getValue() == null ? "" : (String)paramEntry.getValue(), Charset.forName("UTF-8")));
                    System.out.println(new StringBuilder().append((String)paramEntry.getKey()).append("------------").append(String.valueOf(paramEntry.getValue())).toString());
                }
            }

            if ((fileMap != null) && (!fileMap.isEmpty())) {
                for (Map.Entry fileEntry : fileMap.entrySet()) {
                    multipartEntity.addPart((String)fileEntry.getKey(), new FileBody((File)fileEntry.getValue()));
                }
            }

            httpPost.setEntity(multipartEntity);

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(new StringBuilder().append("与[").append(reqURL).append("]通信过程中发生异常,堆栈信息如下").toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendPostRequest(String reqURL, Map<String, String> params, Map<String, String> headerParams, String encodeCharset, String decodeCharset)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();

        System.out.println(reqURL);
        HttpPost httpPost = new HttpPost(reqURL);
        List formParams = new ArrayList();
        if ((headerParams != null) && (headerParams.size() > 0)) {
            for (Map.Entry entry : headerParams.entrySet()) {
                httpPost.setHeader((String)entry.getKey(), (String)entry.getValue());
            }
        }
        for (Map.Entry entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair((String)entry.getKey(), String.valueOf(entry.getValue())));
            System.out.println(new StringBuilder().append((String)entry.getKey()).append("------------").append(String.valueOf(entry.getValue())).toString());
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset == null ? "UTF-8" : encodeCharset));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(new StringBuilder().append("与[").append(reqURL).append("]通信过程中发生异常,堆栈信息如下").toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendPutRequest(String reqURL, Map<String, String> params, Map<String, String> headerParams, String encodeCharset, String decodeCharset)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(reqURL);
        List formParams = new ArrayList();
        if ((headerParams != null) && (headerParams.size() > 0)) {
            for (Map.Entry entry : headerParams.entrySet()) {
                httpPut.setHeader((String)entry.getKey(), (String)entry.getValue());
            }
        }
        for (Map.Entry entry : params.entrySet())
            formParams.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
        try
        {
            httpPut.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset == null ? "UTF-8" : encodeCharset));

            HttpResponse response = httpClient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(new StringBuilder().append("与[").append(reqURL).append("]通信过程中发生异常,堆栈信息如下").toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendPutRequest(String reqURL, Map<String, String> params, String encodeCharset, String decodeCharset)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();

        HttpPut httpPut = new HttpPut(reqURL);
        List formParams = new ArrayList();
        for (Map.Entry entry : params.entrySet())
            formParams.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
        try
        {
            httpPut.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset == null ? "UTF-8" : encodeCharset));

            HttpResponse response = httpClient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(new StringBuilder().append("与[").append(reqURL).append("]通信过程中发生异常,堆栈信息如下").toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendPostSSLRequest(String reqURL, Map<String, String> params)
    {
        return sendPostSSLRequest(reqURL, params, null, null);
    }

    public static String sendPostSSLRequest(String reqURL, Map<String, String> params, String encodeCharset, String decodeCharset)
    {
        String responseContent = "";
        HttpClient httpClient = new DefaultHttpClient();
        X509TrustManager xtm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { xtm }, null);
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

            HttpPost httpPost = new HttpPost(reqURL);
            List formParams = new ArrayList();
            for (Map.Entry entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset == null ? "UTF-8" : encodeCharset));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(new StringBuilder().append("与[").append(reqURL).append("]通信过程中发生异常,堆栈信息为").toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendPostRequestByJava(String reqURL, Map<String, String> params)
    {
        StringBuilder sendData = new StringBuilder();
        for (Map.Entry entry : params.entrySet()) {
            sendData.append((String)entry.getKey()).append("=").append((String)entry.getValue()).append("&");
        }
        if (sendData.length() > 0) {
            sendData.setLength(sendData.length() - 1);
        }
        return sendPostRequestByJava(reqURL, sendData.toString());
    }

    public static String sendPostRequestByJava(String reqURL, String sendData)
    {
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null;
        InputStream in = null;
        int httpStatusCode = 0;
        try {
            URL sendUrl = new URL(reqURL);
            httpURLConnection = (HttpURLConnection)sendUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);

            out = httpURLConnection.getOutputStream();
            out.write(sendData.toString().getBytes());

            out.flush();

            httpStatusCode = httpURLConnection.getResponseCode();

            in = httpURLConnection.getInputStream();
            byte[] byteDatas = new byte[in.available()];
            in.read(byteDatas);
            return new StringBuilder().append(new String(byteDatas)).append("`").append(httpStatusCode).toString();
        }
        catch (Exception e)
        {
            byte[] byteDatas;
            LOGGER.error(e.getMessage());
            return new StringBuilder().append("Failed`").append(httpStatusCode).toString();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    LOGGER.error("关闭输出流时发生异常,堆栈信息如下", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    LOGGER.error("关闭输入流时发生异常,堆栈信息如下", e);
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                httpURLConnection = null;
            }
        }
    }

    public static String sendPostRequestJson(String reqURL, Object data, String encodeCharset, String decodeCharset)
    {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(reqURL);
        try
        {
            StringEntity se = new StringEntity(data.toString(), encodeCharset == null ? "UTF-8" : encodeCharset);
            se.setContentEncoding(encodeCharset == null ? "UTF-8" : encodeCharset);
            se.setContentType("application/json");
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(new StringBuilder().append("与[").append(reqURL).append("]通信过程中发生异常,堆栈信息如下").toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static String sendPostSSLRequestJson(String reqURL, Object data)
    {
        return sendPostSSLRequestJson(reqURL, null, null, data, null, null);
    }

    public static String sendPostSSLWithHeadRequestJson(String reqURL, Map<String, String> heads, Object data)
    {
        return sendPostSSLRequestJson(reqURL, heads, null, data, null, null);
    }

    public static String sendPostSSLRequestJson(String reqURL, Map<String, String> params, Object data)
    {
        return sendPostSSLRequestJson(reqURL, null, params, data, null, null);
    }

    public static String sendPostSSLRequestJson(String reqURL, Map<String, String> heads, Map<String, String> params, Object data, String encodeCharset, String decodeCharset)
    {
        String responseContent = "";
        HttpClient httpClient = new DefaultHttpClient();
        X509TrustManager xtm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { xtm }, null);
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

            HttpPost httpPost = new HttpPost(reqURL);
            Iterator localIterator;
            if ((heads != null) && (heads.size() > 0))
                for (localIterator = heads.entrySet().iterator(); localIterator.hasNext(); ) {
                    Map.Entry entry = (Map.Entry)localIterator.next();
                    httpPost.addHeader((String)entry.getKey(), (String)entry.getValue());
                }
            Map.Entry entry;
            if ((params != null) && (params.size() > 0)) {
                Object formParams = new ArrayList();
                for (Map.Entry parmsEntry : params.entrySet()) {
                    ((List)formParams).add(new BasicNameValuePair((String)parmsEntry.getKey(), (String)parmsEntry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity((List)formParams, encodeCharset == null ? "UTF-8" : encodeCharset));
            }

            if (data != null) {
                StringEntity se = new StringEntity(data.toString(), encodeCharset == null ? "UTF-8" : encodeCharset);
                se.setContentEncoding(encodeCharset == null ? "UTF-8" : encodeCharset);
                se.setContentType("application/json");
                httpPost.setEntity(se);
            }

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LOGGER.error(new StringBuilder().append("与[").append(reqURL).append("]通信过程中发生异常,堆栈信息为").toString(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }
}

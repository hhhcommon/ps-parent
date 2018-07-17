package com.adpanshi.cashloan.common.http;

import java.util.Map;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public interface HttpHandle {
    public abstract HttpResult sendSSLGetRequest(String paramString, Map<String, String> paramMap);

    public abstract HttpResult sendSSLGetRequest(String paramString1, Map<String, String> paramMap1, Map<String, String> paramMap2, String paramString2);

    public abstract HttpResult sendGetRequest(String paramString, Map<String, String> paramMap);

    public abstract HttpResult sendGetRequest(String paramString1, Map<String, String> paramMap1, Map<String, String> paramMap2, String paramString2, boolean paramBoolean1, boolean paramBoolean2);

    public abstract HttpResult sendSSLPostRequest(String paramString, Map<String, String> paramMap);

    public abstract HttpResult sendSSLPostRequest(String paramString1, Map<String, String> paramMap1, Map<String, String> paramMap2, String paramString2);

    public abstract HttpResult sendPostRequest(String paramString, Map<String, String> paramMap);

    public abstract HttpResult sendPostRequest(String paramString1, Map<String, String> paramMap1, Map<String, String> paramMap2, String paramString2, boolean paramBoolean);

    public abstract HttpResult sendSSLPostRequestWithXml(String paramString1, String paramString2);

    public abstract HttpResult sendSSLPostRequestWithJSON(String paramString, Object paramObject);

    public abstract HttpResult sendSSLPostRequestWithJSON(String paramString1, Map<String, String> paramMap, Object paramObject, String paramString2);

    public abstract HttpResult sendPostRequestWithJSON(String paramString, Object paramObject);

    public abstract HttpResult sendPostRequestWithJSON(String paramString, Object paramObject, int paramInt1, int paramInt2);

    public abstract HttpResult sendPostRequestWithJSON(String paramString1, Map<String, String> paramMap, Object paramObject, String paramString2, boolean paramBoolean);

    public abstract HttpResult sendPostRequestWithJSON(String paramString1, Map<String, String> paramMap, Object paramObject, String paramString2, boolean paramBoolean, int paramInt1, int paramInt2);

    public abstract HttpResult sendSSLPutRequest(String paramString, Map<String, String> paramMap);

    public abstract HttpResult sendSSLPutRequest(String paramString1, Map<String, String> paramMap1, Map<String, String> paramMap2, String paramString2);

    public abstract HttpResult sendPutRequest(String paramString, Map<String, String> paramMap);

    public abstract HttpResult sendPutRequest(String paramString1, Map<String, String> paramMap1, Map<String, String> paramMap2, String paramString2, boolean paramBoolean);

    public abstract HttpResult sendSSLPutRequestWithJSON(String paramString, Object paramObject);

    public abstract HttpResult sendSSLPutRequestWithJSON(String paramString1, Map<String, String> paramMap, Object paramObject, String paramString2);

    public abstract HttpResult sendPutRequestWithJSON(String paramString, Object paramObject);

    public abstract HttpResult sendPutRequestWithJSON(String paramString1, Map<String, String> paramMap, Object paramObject, String paramString2, boolean paramBoolean);
}

package com.adpanshi.cashloan.common.http;

import com.adpanshi.cashloan.common.http.client.HttpClientHandle;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class HttpHandleFactory
{
    public static HttpHandle getDefaultHandle()
    {
        try
        {
            Class.forName("org.apache.http.client.HttpClient");
            return new HttpClientHandle();
        } catch (Exception localException) {
        }
        return null;
    }
}

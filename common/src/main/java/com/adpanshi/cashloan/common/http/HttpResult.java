package com.adpanshi.cashloan.common.http;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class HttpResult {
    private Integer code;
    private String body;

    public HttpResult(Integer code, String body)
    {
        this.code = code;
        this.body = body;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

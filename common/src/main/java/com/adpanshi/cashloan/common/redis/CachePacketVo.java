package com.adpanshi.cashloan.common.redis;

import java.io.Serializable;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class CachePacketVo implements Serializable
{
    private static final long serialVersionUID = -6124743248098084617L;
    private Long expire_second;
    private Object obj;

    public Long getExpire_second()
    {
        return this.expire_second;
    }

    public void setExpire_second(Long expire_second) {
        this.expire_second = expire_second;
    }

    public Object getObj() {
        return this.obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}

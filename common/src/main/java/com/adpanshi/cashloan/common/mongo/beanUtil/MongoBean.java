package com.adpanshi.cashloan.common.mongo.beanUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class MongoBean  implements Cloneable
{
    public Object clone()
    {
        try
        {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            MongoKit.INSTANCE.error("MongoBean.class", e.getMessage());
        }
        return this;
    }

    public String toString()
    {
        return JSON.toJSONString(this);
    }

    public Map toMap() {
        return MongoKit.INSTANCE.toMap(this);
    }

    public JSONObject toJSONObject() {
        return (JSONObject)JSON.toJSON(this);
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}

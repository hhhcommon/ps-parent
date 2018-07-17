package com.adpanshi.cashloan.common.redis;

import com.adpanshi.cashloan.common.utils.JSONUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import javax.annotation.Resource;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class RedisExpireCache {

    @Resource
    private RedisUtil redisUtil;

    public <T> T getString(String key, Class<T> cls)
    {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String idValue = this.redisUtil.getString(key);
        CachePacketVo cachePacketVo = (CachePacketVo)JSON.parseObject(idValue, new TypeReference()
                {
                }
                , new Feature[0]);
        if ((cachePacketVo == null) || (cachePacketVo.getObj() == null) || (cachePacketVo.getExpire_second() == null) ||
                (cachePacketVo
                        .getExpire_second().longValue() <= System.currentTimeMillis())) {
            if (cachePacketVo != null) {
                this.redisUtil.delKey(key);
            }
            return null;
        }
        return JSON.parseObject(cachePacketVo.getObj().toString(), cls);
    }

    public void putString(String key, String value, Integer expire)
    {
        if (!StringUtils.isEmpty(value)) {
            CachePacketVo cachePacketVo = new CachePacketVo();
            cachePacketVo.setExpire_second(Long.valueOf(System.currentTimeMillis() + expire.intValue()));
            cachePacketVo.setObj(value);
            this.redisUtil.setString(key, JSONUtils.toJSON(cachePacketVo));
        }
    }
}

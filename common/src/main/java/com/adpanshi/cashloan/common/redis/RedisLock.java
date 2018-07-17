package com.adpanshi.cashloan.common.redis;


import java.util.Random;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class RedisLock
{
    public static final String LOCKED = "TRUE";
    public static final long MILLI_NANO_CONVERSION = 1000000L;
    public static final long DEFAULT_TIME_OUT = 1000L;
    public static final Random RANDOM = new Random();
    public static final int EXPIRE = 180;
    private ShardedJedisPool shardedJedisPool;
    private ShardedJedis jedis;
    private String key;
    private boolean locked = false;

    public RedisLock(String key, ShardedJedisPool shardedJedisPool)
    {
        this.key = (key + "_lock");
        this.shardedJedisPool = shardedJedisPool;
        this.jedis = this.shardedJedisPool.getResource();
    }

    public boolean lock(long timeout)
    {
        long nano = System.nanoTime();
        timeout *= 1000000L;
        try {
            while (System.nanoTime() - nano < timeout) {
                if (this.jedis.setnx(this.key, "TRUE").longValue() == 1L) {
                    this.jedis.expire(this.key, 180);
                    this.locked = true;
                    return this.locked;
                }

                Thread.sleep(3L, RANDOM.nextInt(500));
            }
        } catch (Exception e) {
            throw new RuntimeException("Locking error", e);
        }
        return false;
    }

    public boolean lock(long timeout, int expire)
    {
        long nano = System.nanoTime();
        timeout *= 1000000L;
        try {
            while (System.nanoTime() - nano < timeout) {
                if (this.jedis.setnx(this.key, "TRUE").longValue() == 1L) {
                    this.jedis.expire(this.key, expire);
                    this.locked = true;
                    return this.locked;
                }

                Thread.sleep(3L, RANDOM.nextInt(500));
            }
        } catch (Exception e) {
            throw new RuntimeException("Locking error", e);
        }
        return false;
    }

    public boolean lock()
    {
        return lock(1000L);
    }

    public void unlock()
    {
        try
        {
            if (this.locked) {
                this.jedis.del(this.key);
            }

            this.shardedJedisPool.returnResource(this.jedis); } finally { this.shardedJedisPool.returnResource(this.jedis); }

    }
}

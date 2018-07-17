package com.adpanshi.cashloan.common.redis;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class RedisUtil
{
    private ShardedJedisPool shardedJedisPool;

    public long delKeysLike(final String likeKey)
    {
        return ((Long)new Executor(this.shardedJedisPool, likeKey)
        {
            Long execute()
            {
                Collection jedisC = this.jedis.getAllShards();
                Iterator iter = jedisC.iterator();
                long count = 0L;
                while (iter.hasNext()) {
                    Jedis _jedis = (Jedis)iter.next();
                    Set keys = _jedis.keys(likeKey + "*");
                    count += _jedis.del((String[])keys.toArray(new String[keys.size()])).longValue();
                }
                return Long.valueOf(count);
            }
        }
                .getResult()).longValue();
    }

    public Long delKey(final String key)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.del(key);
            }
        }
                .getResult();
    }

    public Long delKeys(final String[] keys)
    {
        return (Long)new Executor(this.shardedJedisPool, keys)
        {
            Long execute()
            {
                Collection jedisC = this.jedis.getAllShards();
                Iterator iter = jedisC.iterator();
                long count = 0L;
                while (iter.hasNext()) {
                    Jedis _jedis = (Jedis)iter.next();
                    count += _jedis.del(keys).longValue();
                }
                return Long.valueOf(count);
            }
        }
                .getResult();
    }

    public Long hdelFields(final String key, final String[] fields)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.hdel(key, fields);
            }
        }
                .getResult();
    }

    public Long expire(final String key, final int expire)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.expire(key, expire);
            }
        }
                .getResult();
    }

    public long makeId(final String key)
    {
        return ((Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                long id = this.jedis.incr(key).longValue();
                if (id + 75807L >= 9223372036854775807L)
                {
                    this.jedis.getSet(key, "0");
                }
                return Long.valueOf(id);
            }
        }
                .getResult()).longValue();
    }

    public String setString(final String key, final String value)
    {
        return (String)new Executor(this.shardedJedisPool, key)
        {
            String execute()
            {
                return this.jedis.set(key, value);
            }
        }
                .getResult();
    }

    public String setString(final String key, final String value, final int expire)
    {
        return (String)new Executor(this.shardedJedisPool, key)
        {
            String execute()
            {
                return this.jedis.setex(key, expire, value);
            }
        }
                .getResult();
    }

    public Long setStringIfNotExists(final String key, final String value)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.setnx(key, value);
            }
        }
                .getResult();
    }

    public String getSet(final String key, final String value)
    {
        return (String)new Executor(this.shardedJedisPool, key)
        {
            String execute() {
                return this.jedis.getSet(key, value);
            }
        }
                .getResult();
    }

    public Long incr(final String key)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute() {
                return this.jedis.incr(key);
            }
        }
                .getResult();
    }

    public String getString(final String key)
    {
        return (String)new Executor(this.shardedJedisPool, key)
        {
            String execute()
            {
                return this.jedis.get(key);
            }
        }
                .getResult();
    }

    public List<Object> batchSetString(final List<Pair<String, String>> pairs)
    {
        return (List)new Executor(this.shardedJedisPool, pairs)
        {
            List<Object> execute()
            {
                ShardedJedisPipeline pipeline = this.jedis.pipelined();
                for (RedisUtil.Pair pair : pairs) {
                    pipeline.set((String)pair.getKey(), (String)pair.getValue());
                }
                return pipeline.syncAndReturnAll();
            }
        }
                .getResult();
    }

    public List<String> batchGetString(final String[] keys)
    {
        return (List)new Executor(this.shardedJedisPool, keys)
        {
            List<String> execute()
            {
                ShardedJedisPipeline pipeline = this.jedis.pipelined();
                List result = new ArrayList(keys.length);
                List responses = new ArrayList(keys.length);
                for (String key : keys) {
                    responses.add(pipeline.get(key));
                }
                pipeline.sync();
                for (Object resp : responses) {
                    result.add(((Pair<String, String[]>)resp).getKey());
                }
                return result;
            }
        }
                .getResult();
    }

    public Long hashSet(final String key, final String field, final String value)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.hset(key, field, value);
            }
        }
                .getResult();
    }

    public Long hashSet(final String key, final String field, final String value, final int expire)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                Pipeline pipeline = ((Jedis)this.jedis.getShard(key)).pipelined();
                Response result = pipeline.hset(key, field, value);
                pipeline.expire(key, expire);
                pipeline.sync();
                return (Long)result.get();
            }
        }
                .getResult();
    }

    public String hashGet(final String key, final String field)
    {
        return (String)new Executor(this.shardedJedisPool, key)
        {
            String execute()
            {
                return this.jedis.hget(key, field);
            }
        }
                .getResult();
    }

    public String hashGet(final String key, final String field, final int expire)
    {
        return (String)new Executor(this.shardedJedisPool, key)
        {
            String execute()
            {
                Pipeline pipeline = ((Jedis)this.jedis.getShard(key)).pipelined();
                Response result = pipeline.hget(key, field);
                pipeline.expire(key, expire);
                pipeline.sync();
                return (String)result.get();
            }
        }
                .getResult();
    }

    public String hashMultipleSet(final String key, final Map<String, String> hash)
    {
        return (String)new Executor(this.shardedJedisPool, key)
        {
            String execute()
            {
                return this.jedis.hmset(key, hash);
            }
        }
                .getResult();
    }

    public String hashMultipleSet(final String key, final Map<String, String> hash, final int expire)
    {
        return (String)new Executor(this.shardedJedisPool, key)
        {
            String execute()
            {
                Pipeline pipeline = ((Jedis)this.jedis.getShard(key)).pipelined();
                Response result = pipeline.hmset(key, hash);
                pipeline.expire(key, expire);
                pipeline.sync();
                return (String)result.get();
            }
        }
                .getResult();
    }

    public List<String> hashMultipleGet(final String key, final String[] fields)
    {
        return (List)new Executor(this.shardedJedisPool, key)
        {
            List<String> execute()
            {
                return this.jedis.hmget(key, fields);
            }
        }
                .getResult();
    }

    public List<String> hashMultipleGet(final String key, final int expire, final String[] fields)
    {
        return (List)new Executor(this.shardedJedisPool, key)
        {
            List<String> execute()
            {
                Pipeline pipeline = ((Jedis)this.jedis.getShard(key)).pipelined();
                Response result = pipeline.hmget(key, fields);
                pipeline.expire(key, expire);
                pipeline.sync();
                return (List)result.get();
            }
        }
                .getResult();
    }

    public List<Object> batchHashMultipleSet(final List<Pair<String, Map<String, String>>> pairs)
    {
        return (List)new Executor(this.shardedJedisPool, pairs)
        {
            List<Object> execute()
            {
                ShardedJedisPipeline pipeline = this.jedis.pipelined();
                for (RedisUtil.Pair pair : pairs) {
                    pipeline.hmset((String)pair.getKey(), (Map)pair.getValue());
                }
                return pipeline.syncAndReturnAll();
            }
        }
                .getResult();
    }

    public List<Object> batchHashMultipleSet(final Map<String, Map<String, String>> data)
    {
        return (List)new Executor(this.shardedJedisPool, data)
        {
            List<Object> execute()
            {
                ShardedJedisPipeline pipeline = this.jedis.pipelined();
                for (Map.Entry iter : data.entrySet()) {
                    pipeline.hmset((String)iter.getKey(), (Map)iter.getValue());
                }
                return pipeline.syncAndReturnAll();
            }
        }
                .getResult();
    }

    public List<List<String>> batchHashMultipleGet(final List<Pair<String, String[]>> pairs)
    {
        return (List)new Executor(this.shardedJedisPool, pairs)
        {
            List<List<String>> execute()
            {
                ShardedJedisPipeline pipeline = this.jedis.pipelined();
                List result = new ArrayList(pairs.size());
                List responses = new ArrayList(pairs.size());
                for (RedisUtil.Pair pair : pairs) {
                    responses.add(pipeline.hmget((String)pair.getKey(), (String[])pair.getValue()));
                }
                pipeline.sync();
                for (Object resp : responses) {
                    result.add(((Pair<String, String[]>)resp).getKey());
                }
                return result;
            }
        }
                .getResult();
    }

    public Map<String, String> hashGetAll(final String key)
    {
        return (Map)new Executor(this.shardedJedisPool, key)
        {
            Map<String, String> execute()
            {
                return this.jedis.hgetAll(key);
            }
        }
                .getResult();
    }

    public Map<String, String> hashGetAll(final String key, final int expire)
    {
        return (Map)new Executor(this.shardedJedisPool, key)
        {
            Map<String, String> execute()
            {
                Pipeline pipeline = ((Jedis)this.jedis.getShard(key)).pipelined();
                Response result = pipeline.hgetAll(key);
                pipeline.expire(key, expire);
                pipeline.sync();
                return (Map)result.get();
            }
        }
                .getResult();
    }

    public List<Map<String, String>> batchHashGetAll(final String[] keys)
    {
        return (List)new Executor(this.shardedJedisPool, keys)
        {
            List<Map<String, String>> execute()
            {
                ShardedJedisPipeline pipeline = this.jedis.pipelined();
                List result = new ArrayList(keys.length);
                List responses = new ArrayList(keys.length);
                for (String key : keys) {
                    responses.add(pipeline.hgetAll(key));
                }
                pipeline.sync();
                for (Object resp : responses) {
                    result.add(((Pair<String, String[]>)resp).getKey());
                }
                return result;
            }
        }
                .getResult();
    }

    public Map<String, Map<String, String>> batchHashGetAllForMap(final String[] keys)
    {
        return (Map)new Executor(this.shardedJedisPool, keys)
        {
            Map<String, Map<String, String>> execute()
            {
                ShardedJedisPipeline pipeline = this.jedis.pipelined();

                int capacity = 1;
                while ((int)(capacity * 0.75D) <= keys.length) {
                    capacity <<= 1;
                }
                Map result = new HashMap(capacity);
                List responses = new ArrayList(keys.length);
                for (String key : keys) {
                    responses.add(pipeline.hgetAll(key));
                }
                pipeline.sync();
                for (int i = 0; i < keys.length; i++) {
                    result.put(keys[i], ((Response)responses.get(i)).get());
                }
                return result;
            }
        }
                .getResult();
    }

    public Long listPushEnd(final String key, final String[] values)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.rpush(key, values);
            }
        }
                .getResult();
    }

    public Long listPushHead(final String key, final String value)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.lpush(key, new String[] { value });
            }
        }
                .getResult();
    }

    public String listPopEnd(final String key)
    {
        return (String)new Executor(this.shardedJedisPool, key)
        {
            String execute()
            {
                return this.jedis.rpop(key);
            }
        }
                .getResult();
    }

    public String listPopHead(final String key)
    {
        return (String)new Executor(this.shardedJedisPool, key)
        {
            String execute()
            {
                return this.jedis.lpop(key);
            }
        }
                .getResult();
    }

    public Long listPushHeadAndTrim(final String key, final String value, final long size)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                Pipeline pipeline = ((Jedis)this.jedis.getShard(key)).pipelined();
                Response result = pipeline.lpush(key, new String[] { value });

                pipeline.ltrim(key, 0L, size - 1L);
                pipeline.sync();
                return (Long)result.get();
            }
        }
                .getResult();
    }

    public void batchListPushTail(final String key, final String[] values, final boolean delOld)
    {
        new Executor(this.shardedJedisPool, delOld)
        {
            Object execute()
            {
                if (delOld) {
                    RedisLock lock = new RedisLock(key, this.shardedJedisPool);
                    lock.lock();
                    try {
                        Pipeline pipeline = ((Jedis)this.jedis.getShard(key)).pipelined();
                        pipeline.del(key);
                        for (String value : values) {
                            pipeline.rpush(key, new String[] { value });
                        }
                        pipeline.sync();
                    } finally {
                        lock.unlock();
                    }
                } else {
                    this.jedis.rpush(key, values);
                }
                return null;
            }
        }
                .getResult();
    }

    public Object updateListInTransaction(final String key, final List<String> values)
    {
        return new Executor(this.shardedJedisPool, key)
        {
            Object execute()
            {
                Transaction transaction = ((Jedis)this.jedis.getShard(key)).multi();
                transaction.del(key);
                for (String value : values) {
                    transaction.rpush(key, new String[] { value });
                }
                transaction.exec();
                return null;
            }
        }
                .getResult();
    }

    public Long insertListIfNotExists(final String key, final String[] values)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                RedisLock lock = new RedisLock(key, this.shardedJedisPool);
                lock.lock();
                try {
                    if (!this.jedis.exists(key).booleanValue()) {
                        Long localLong = this.jedis.rpush(key, values);

                        return localLong; } lock.unlock(); } finally { lock.unlock(); }

                return Long.valueOf(0L);
            }
        }
                .getResult();
    }

    public List<String> listGetAll(final String key)
    {
        return (List)new Executor(this.shardedJedisPool, key)
        {
            List<String> execute()
            {
                return this.jedis.lrange(key, 0L, -1L);
            }
        }
                .getResult();
    }

    public List<String> listRange(final String key, final long beginIndex, final long endIndex)
    {
        return (List)new Executor(this.shardedJedisPool, key)
        {
            List<String> execute()
            {
                return this.jedis.lrange(key, beginIndex, endIndex - 1L);
            }
        }
                .getResult();
    }

    public Map<String, List<String>> batchGetAllList(final List<String> keys)
    {
        return (Map)new Executor(this.shardedJedisPool, keys)
        {
            Map<String, List<String>> execute()
            {
                ShardedJedisPipeline pipeline = this.jedis.pipelined();
                Map result = new HashMap();
                List responses = new ArrayList(keys.size());
                for (String key : keys) {
                    responses.add(pipeline.lrange(key, 0L, -1L));
                }
                pipeline.sync();
                for (int i = 0; i < keys.size(); i++) {
                    result.put(keys.get(i), ((Response)responses.get(i)).get());
                }
                return result;
            }
        }
                .getResult();
    }

    public Long listLrem(final String key, final long count, final String value)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute() {
                return this.jedis.lrem(key, count, value);
            }
        }
                .getResult();
    }

    public Long publish(final String channel, final String message)
    {
        return (Long)new Executor(this.shardedJedisPool, channel)
        {
            Long execute()
            {
                Jedis _jedis = (Jedis)this.jedis.getShard(channel);
                return _jedis.publish(channel, message);
            }
        }
                .getResult();
    }

    public void subscribe(final JedisPubSub jedisPubSub, final String channel)
    {
        new Executor(this.shardedJedisPool, channel)
        {
            Object execute()
            {
                Jedis _jedis = (Jedis)this.jedis.getShard(channel);

                _jedis.subscribe(jedisPubSub, new String[] { channel });
                return null;
            }
        }
                .getResult();
    }

    public void unSubscribe(JedisPubSub jedisPubSub)
    {
        jedisPubSub.unsubscribe();
    }

    public Long addWithSortedSet(final String key, final double score,final String member)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.zadd(key, score, member);
            }
        }
                .getResult();
    }

    public Set<String> revrangeByScoreWithSortedSet(final String key, final double max, final double min)
    {
        return (Set)new Executor(this.shardedJedisPool, key)
        {
            Set<String> execute()
            {
                return this.jedis.zrevrangeByScore(key, max, min);
            }
        }
                .getResult();
    }

    public Integer sadd(final String key, final String value)
    {
        return Integer.valueOf(((Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.sadd(key, new String[] { value });
            }
        }
                .getResult()).intValue());
    }

    public Long hlen(final String key)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.hlen(key);
            }
        }
                .getResult();
    }

    public Long llen(final String key)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute() {
                return this.jedis.llen(key);
            }
        }
                .getResult();
    }

    public Long zadd(final String key, final double score, final String member)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute()
            {
                return this.jedis.zadd(key, score, member);
            }
        }
                .getResult();
    }

    public Long zremrangeByScore(final String key, final double start, final double end)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute() {
                return this.jedis.zremrangeByScore(key, start, end);
            }
        }
                .getResult();
    }

    public Long zrem(final String key, final String[] members)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute() {
                return this.jedis.zrem(key, members); }
        }.getResult();
    }

    public Boolean ssismember(final String key, final String value)
    {
        return (Boolean)new Executor(this.shardedJedisPool, key)
        {
            Boolean execute() {
                return this.jedis.sismember(key, value); }
        }.getResult();
    }

    public Long sadd(final String key, final String[] members)
    {
        return (Long)new Executor(this.shardedJedisPool, key)
        {
            Long execute() {
                return this.jedis.sadd(key, members); }
        }.getResult();
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool)
    {
        this.shardedJedisPool = shardedJedisPool;
    }

    public <K, V> Pair<K, V> makePair(K key, V value)
    {
        return new Pair(key, value);
    }

    public RedisLock initLock(final String key)
    {
        return (RedisLock)new Executor(this.shardedJedisPool, key)
        {
            RedisLock execute() {
                return new RedisLock(key, this.shardedJedisPool);
            }
        }
                .getResult();
    }

    public class Pair<K, V>
    {
        private K key;
        private V value;

        public Pair(K key, V value)
        {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return this.value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    abstract class Executor<T>
    {
        ShardedJedis jedis;
        ShardedJedisPool shardedJedisPool;

        public Executor(ShardedJedisPool shardedJedisPool)
        {
            this.shardedJedisPool = shardedJedisPool;
            this.jedis = this.shardedJedisPool.getResource();
        }

        public Executor(ShardedJedisPool shardedJedisPool, T key)
        {
            this.shardedJedisPool = shardedJedisPool;
            this.jedis = this.shardedJedisPool.getResource();
        }

        abstract T execute();

        public T getResult()
        {
            T result = null;
            try {
                result = execute();
            } catch (Throwable e) {
                throw new RuntimeException("Redis execute exception", e);
            } finally {
                if (this.jedis != null) {
                    this.jedis.close();
                }
            }
            return result;
        }
    }
}

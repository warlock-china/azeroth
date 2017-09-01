package cn.com.warlock.cache.command;

import static cn.com.warlock.cache.redis.JedisProviderFactory.getBinaryJedisClusterCommands;
import static cn.com.warlock.cache.redis.JedisProviderFactory.getBinaryJedisCommands;
import static cn.com.warlock.cache.redis.JedisProviderFactory.getJedisProvider;
import static cn.com.warlock.cache.redis.JedisProviderFactory.isCluster;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.util.SafeEncoder;

/**
 *
 * @author warlock
 * @version $Id: RedisHashMap.java, v 0.1 Apr 13, 2017 5:45:15 PM warlock Exp $
 */
public class RedisHashMap extends RedisCollection {

    public RedisHashMap(String key) {
        super(key);
    }

    /**
     * @param key
     * @param expireTime 超时时间(秒) 小于等于0 为永久缓存
     */
    public RedisHashMap(String key, long expireTime) {
        super(key, expireTime);
    }

    /**
     * 指定组名
     *
     * @param key
     * @param groupName
     */
    public RedisHashMap(String key, String groupName) {
        super(key, groupName);
    }

    /**
     *
     * @param key
     * @param groupName 分组名
     * @param expireTime 超时时间(秒) 小于等于0 为永久缓存
     */
    public RedisHashMap(String key, String groupName, long expireTime) {
        super(key, groupName, expireTime);
    }

    /**
     * 设置hash缓存
     *
     * @param datas
     * @return
     */
    public <T> boolean set(Map<String, T> datas) {
        if (datas == null || datas.isEmpty()) { return false; }
        Map<byte[], byte[]> newDatas = new HashMap<>();
        Set<String> keySet = datas.keySet();
        for (String key : keySet) {
            if (datas.get(key) == null) { continue; }
            newDatas.put(SafeEncoder.encode(key), valueSerialize(datas.get(key)));
        }

        boolean result = false;
        try {
            if (isCluster(groupName)) {
                result = getBinaryJedisClusterCommands(groupName).hmset(key, newDatas)
                        .equals(RESP_OK);
            } else {
                result = getBinaryJedisCommands(groupName).hmset(key, newDatas).equals(RESP_OK);
            }
            //设置超时时间
            if (result) { setExpireIfNot(expireTime); }
            return result;
        } finally {
            getJedisProvider(groupName).release();
        }

    }

    /**
     * 获取所有值
     *
     * @return
     */
    public <T> Map<String, T> getAll() {
        try {
            Map<byte[], byte[]> datas = null;
            Map<String, T> result = new HashMap<>();
            if (isCluster(groupName)) {
                datas = getBinaryJedisClusterCommands(groupName).hgetAll(key);
            } else {
                datas = getBinaryJedisCommands(groupName).hgetAll(key);
            }

            Iterator<Map.Entry<byte[], byte[]>> it = datas.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<byte[], byte[]> entry = it.next();
                result.put(SafeEncoder.encode(entry.getKey()),
                        (T) valueDerialize(entry.getValue()));
            }
            return result;
        } finally {
            getJedisProvider(groupName).release();
        }

    }

    /**
     * 查看缓存hash是否包含某个key
     *
     * @param field
     * @return
     */
    public boolean containsKey(String field) {
        try {
            if (isCluster(groupName)) {
                return getBinaryJedisClusterCommands(groupName).hexists(key,
                        SafeEncoder.encode(field));
            } else {
                return getBinaryJedisCommands(groupName).hexists(key, SafeEncoder.encode(field));
            }
        } finally {
            getJedisProvider(groupName).release();
        }
    }

    /**
     * 设置ç
     *
     * @param field
     * @param value
     * @return
     */
    public boolean set(String field, Object value) {
        boolean result = false;
        if (value == null) { return false; }
        //返回值（1:新字段被设置,0:已经存在值被更新）
        try {
            if (isCluster(groupName)) {
                result = getBinaryJedisClusterCommands(groupName).hset(key,
                        SafeEncoder.encode(field), valueSerialize(value)) >= 0;
            } else {
                result = getBinaryJedisCommands(groupName).hset(key, SafeEncoder.encode(field),
                        valueSerialize(value)) >= 0;
            }
            //设置超时时间
            if (result) { setExpireIfNot(expireTime); }
            return result;
        } finally {
            getJedisProvider(groupName).release();
        }
    }

    /**
     * 移除hash缓存中的指定值
     *
     * @param field
     * @return
     */
    public boolean remove(String field) {
        try {
            if (isCluster(groupName)) {
                return getBinaryJedisClusterCommands(groupName).hdel(key, SafeEncoder.encode(field))
                        .equals(RESP_OK);
            } else {
                return getBinaryJedisCommands(groupName).hdel(key, SafeEncoder.encode(field))
                        .equals(RESP_OK);
            }
        } finally {
            getJedisProvider(groupName).release();
        }
    }

    /**
     * 返回长度
     *
     * @return
     */
    public long length() {
        try {
            if (isCluster(groupName)) {
                return getBinaryJedisClusterCommands(groupName).hlen(key);
            } else {
                return getBinaryJedisCommands(groupName).hlen(key);
            }
        } finally {
            getJedisProvider(groupName).release();
        }
    }

    /**
     * 获取一个值
     *
     * @param field
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getOne(String field) {
        return (T) get(field).get(field);
    }

    /**
     * 获取多个key的值
     *
     * @param fields
     * @return
     */
    public <T> Map<String, T> get(String... fields) {
        try {
            List<byte[]> datas = null;
            Map<String, T> result = new HashMap<>();

            byte[][] encodeFileds = SafeEncoder.encodeMany(fields);
            if (isCluster(groupName)) {
                datas = getBinaryJedisClusterCommands(groupName).hmget(key, encodeFileds);
            } else {
                datas = getBinaryJedisCommands(groupName).hmget(key, encodeFileds);
            }
            for (int i = 0; i < fields.length; i++) {
                result.put(fields[i], (T) valueDerialize(datas.get(i)));
            }
            return result;
        } finally {
            getJedisProvider(groupName).release();
        }

    }

}

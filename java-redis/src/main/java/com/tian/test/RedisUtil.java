package com.tian.test;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

import java.io.*;
import java.util.*;

/**
 * Author: qianzhong.fu
 * Date: 2016/04/27
 * Time: 12:44
 */
public class RedisUtil {

    /**
     * redis属性文件路径
     */
    private static final String REDISDB_CONFIG_PATH = "/resource/redis.properties";

    public static final String TYPE_GET = "get";
    public static final String TYPE_SAVE = "save";
    public static final String TYPE_DELETE = "delete";
    public static final String DB_PARAM = "db";
    public static final Integer EXPIRE_SECONDS;

    public static final Integer SESSION_DB;
    public static final Integer DICT_DB;
    public static final Integer XXBK_DB;
    public static final Integer DEFAULT_DB;
    
    /** redis key **/
    public static final String AUTHURI = "SYS:AUTHURI";
    public static final int SEQ_EXPIRE=24*60*60;
    
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private static final Map<Integer, JedisPool> jedisPools = new HashMap<>();

    static {
        PropertiesUtil redisdbProperties = new PropertiesUtil(REDISDB_CONFIG_PATH);

        SESSION_DB = redisdbProperties.getInteger("redis.session.db");
        DICT_DB = redisdbProperties.getInteger("redis.dict.db");
        XXBK_DB = redisdbProperties.getInteger("redis.xxbk.db");
        DEFAULT_DB = redisdbProperties.getInteger("redis.default.db");
        EXPIRE_SECONDS = redisdbProperties.getInteger("redis.expired.time");
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(redisdbProperties.getInteger("redis.maxIdle"));
            jedisPoolConfig.setMaxTotal(redisdbProperties.getInteger("redis.maxTotal"));
            jedisPoolConfig.setMaxWaitMillis(redisdbProperties.getInteger("redis.maxWaitMillis"));

            String host = redisdbProperties.getProperty("redis.host");
            int port = redisdbProperties.getInteger("redis.port");
            int timeout = redisdbProperties.getInteger("redis.timeout");
            String password = redisdbProperties.getProperty("redis.pwd");
            if (StringUtils.isBlank(password))
                password = null;

            JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, SESSION_DB);
            jedisPools.put(SESSION_DB, jedisPool);

            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, DICT_DB);
            jedisPools.put(DICT_DB, jedisPool);

            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, XXBK_DB);
            jedisPools.put(XXBK_DB, jedisPool);

            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, DEFAULT_DB);
            jedisPools.put(DEFAULT_DB, jedisPool);

        } catch (Exception ex) {
            logger.error("Redis Util : initialize jedis pool fail , " + ex);
        }
    }

    
    /**
     * 根据数据库的索引值返回库中数据量
     * @author   zhangchunli
     * @date     2016年12月20日 下午10:19:14
     * @param    db    数据库索引值
     * @return   数据库记录数
     */
    public static Long redisDBCount(Integer db) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Long count = 0L;
        try {
            jedis = jedisPool.getResource();
            count = jedis.dbSize();
        } catch (Exception ex) {
            logger.error("Redis Util : connection fail , " + ex);
        } 
        return count;
    }

    
    
    /**
     * 删除Redis中的所有key
     * 
     * @param db
     * @throws Exception
     */
    public static void flushAll(Integer db) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.flushDB();
        } catch (Exception e) {
            logger.error("Cache清空失败：" + e);
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    public static void setObject(Integer db, String key, Object object) {
        if (key == null || key.isEmpty() || object == null)
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            byte[] objBytes = serialize(object);
            jedis.set(key.getBytes(), objBytes);
            jedis.expire(key.getBytes(), EXPIRE_SECONDS);
        } catch (Exception ex) {
            logger.error("Redis Util : set object fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    /**
     * 设置redis数据过期时间
     * @author maoli
     * @date 2016-6-3 上午8:55:34
     * @param db 数据库实例名
     * @param key key值
     * @param object value对象
     * @param expireSeconds 过期时间（秒）
     */
    public static void setExpireObject(Integer db, String key, Object object, Integer expireSeconds) {
        if (key == null || key.isEmpty() || object == null) {
            return;
        }
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            byte[] objBytes = null;
            if (object instanceof byte[]) {
                objBytes = (byte[]) object;
            } else {
                objBytes = serialize(object);
            }
            jedis.set(key.getBytes(), objBytes);
            jedis.expire(key.getBytes(), expireSeconds);
        } catch (Exception ex) {
            logger.error("Redis Util : set object fail , " + ex);
            if (ex instanceof JedisException) {
                success = false;
            }
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    /**
     * 查询某个数据库中key值对应value
     * @author zhangchunli
     * @date 2016年12月20日 下午10:11:26
     * @param db
     * @param key
     * @return
     */
    public static Object getObject(Integer db, String key) {
        if (key == null || key.isEmpty())
            return null;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        Object result = null;
        try {
            jedis = jedisPool.getResource();
            byte[] objBytes = jedis.get(key.getBytes());
            result = unserialize(objBytes);
        } catch (Exception ex) {
            logger.error("Redis Util : get object fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }

        return result;
    }

    /**
     * 获取字节数组
     * @author maoli
     * @date 2016-6-6 下午6:56:59
     * @param db 数据库名
     * @param key 键
     * @return 字节数组
     */
    public static byte[] getObjectBytes(Integer db, String key) {
        if (key == null || key.isEmpty())
            return null;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        byte[] result = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key.getBytes());
        } catch (Exception ex) {
            logger.error("Redis Util : get object fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
        return result;
    }

    
    public static void delete(Integer db, String key) {
        if (key == null || key.isEmpty())
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception ex) {
            logger.error("Redis Util : delete fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    /**
     * 保存一个对象到redis中并指定过期时间
     * 
     * @param key 键 .
     * @param value 缓存对象 .
     */
    public static void setString(Integer db, String key, String value) {
        if (key == null || key.isEmpty() || value == null)
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, EXPIRE_SECONDS);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    /**
     * 添加一个内容到指定key的hash中
     * 
     * @param key
     * @param map
     */
    public static void setHash(Integer db, String key, Map<String, String> map) {
        if (key == null || key.isEmpty() || map == null || map.size() == 0)
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            for (String field : map.keySet()) {
                jedis.hset(key, field, map.get(field));
//                jedis.expire(key, EXPIRE_SECONDS);
            }
        } catch (Exception ex) {
            logger.error("Redis Util : set hash fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }
    
    public static void setHash(Integer db, String rootKey, String key, String value) {
        if (key == null || key.isEmpty() || rootKey == null || rootKey.isEmpty() || value == null || value.isEmpty() )
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(rootKey, key, value);
//            jedis.expire(key, EXPIRE_SECONDS);
        } catch (Exception ex) {
            logger.error("Redis Util : set hash fail , " + ex);
            if(ex instanceof JedisException)
                success = false;
        }finally {
            returnJedisResource(jedisPool,jedis,success);
        }
    }
    
    /**
     * 用hash结构保存数据
     * @author maoli
     * @date 2016-6-15 上午10:14:39
     * @param db 数据库实例名
     * @param rootKey hash的key
     * @param key hash里面字段名
     * @param value hash里面字段对应的值
     */
    public static void setHashObject(Integer db, String rootKey, String key, Object value) {
        if (key == null || key.isEmpty() || rootKey == null || rootKey.isEmpty() || value == null) {
            return;
        }
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            byte[] objBytes = serialize(value);
            jedis.hset(rootKey.getBytes(), key.getBytes(), objBytes);
        } catch (Exception ex) {
            logger.error("Redis Util : set hash fail , " + ex);
            if (ex instanceof JedisException) {
                success = false;
            }
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    /**
     * 添加一个内容到指定key的list中
     * 
     * @param key
     * @param list
     */
    public static void setList(Integer db, String key, List<String> list) {
        if (key == null || key.isEmpty() || list == null || list.size() == 0)
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            for (String value : list) {
                jedis.rpush(key, value);
                jedis.expire(key, EXPIRE_SECONDS);
            }
        } catch (Exception ex) {
            logger.error("Redis Util : set list fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    /**
     * 添加一个内容到指定key的字符中
     * 
     * @param key
     * @param values
     */
    public static void setList(int db, String key, String[] values) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            Transaction tx = jedis.multi();
            tx.del(key);
            tx.rpush(key, values);
            tx.exec();
        } catch (Exception ex) {
            logger.error("Redis Util : set list fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    public static String getString(Integer db, String key) {
        if (key == null || key.isEmpty())
            return null;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        String result = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            result = jedis.get(key);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }

        return result;
    }

    public static List<String> getList(Integer db, String key) {
        if (key == null || key.isEmpty())
            return null;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        List<String> result = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lrange(key, 0, -1);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }

        return result;
    }

    public static Map<String, String> getHash(Integer db, String key) {
        if (key == null || key.isEmpty())
            return null;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        Map<String, String> result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hgetAll(key);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }

        return result;
    }
    
    public static String getHash(Integer db, String key, String field) {
        if (key == null || key.isEmpty())
            return null;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hget(key, field);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if(ex instanceof JedisException)
                success = false;
        }finally {
            returnJedisResource(jedisPool,jedis,success);
        }

        return result;
    }
    
    /**
     * @author maoli
     * @date 2016-6-15 上午9:03:59
     * @param db 数据库实例名
     * @param key 获取hash的键
     * @param field 获取hash字段名
     * @return 返回对象
     */
    public static Object getHashObject(Integer db, String key, String field) {
        if (key == null || key.isEmpty() || field == null || field.isEmpty()) {
            return null;
        }
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        byte[] result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hget(key.getBytes(), field.getBytes());
            if (result != null) {
                return unserialize(result);
            }
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException) {
                success = false;
            }
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
        return result;
    }

    public static boolean connectRedis(Integer db) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.get("");
            return true;
        } catch (Exception ex) {
            logger.error("Redis Util : connection fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
            throw ex;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }

    }

    public static Set<String> zrange(int db, String key, int start, int stop) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        Set<String> result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zrange(key, start, stop);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }

        return result;
    }

    public static Set<String> zrevrange(int db, String key, int start, int stop) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        Set<String> result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zrevrange(key, start, stop);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }

        return result;
    }

    public static void zadd(int db, String key, long score, String member) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.zadd(key, score, member);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    public static void delete(int db, String... key) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }
    
    /**
     * 删除hash里面的field对应的值
     * @author maoli
     * @date 2016-6-15 上午9:08:34
     * @param db 数据库实例名
     * @param rootkey 获取hash的键
     * @param field hash里面的字段名
     */
    public static void deleteHash(int db, String rootkey, String field) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.hdel(rootkey, field);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException) {
                success = false;
            }
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }
    
    public static void setValueToMap(int db, String key, String field, String value) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }

    }

    public static String hget(int db, String key, String field) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hget(key, field);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
        return result;
    }

    public static void incr(int db, String key) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.incr(key);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }
    
    /**
     *  redis获取自增字段的值
     * @author zhanggangyang
     * @date 2018年3月7日 下午3:46:40
     * @param db 数据库实例
     * @param key  名称
     * @param field 字段
     * @param value 字段值
     * @return
     */
    public static long hincrBy(int db, String key,String field,long value) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        long result=0;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            result=jedis.hincrBy(key, field, value);
        } catch (Exception ex) {
            logger.error("Redis Util : hincrBy , " + ex);
        }finally {
            returnJedisResource(jedisPool, jedis, success);
        }
        return result;
    }

    public static void zincrBy(int db, String key, int score, String member) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            Transaction tx = jedis.multi();
            tx.zincrby(key, score, member);
            tx.zremrangeByScore(key, Integer.MIN_VALUE, 0);
            tx.exec();
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    public static void expire(int db, String key, int expire) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, expire);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    @SuppressWarnings("deprecation")
    public static void setValue(int db, String key, String value, long expiry) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            jedis.psetex(key, (int) expiry, value);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    public static Set<Tuple> zrevrangeByScoreWithScores(int db, String key, double min, double max) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, min, max);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
            return null;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }

    /**
     * 释放redis资源
     * 
     * @param jedis
     */
    @SuppressWarnings("deprecation")
    private static void returnJedisResource(JedisPool jedisPool, Jedis jedis, Boolean success) {
        if (jedisPool != null && jedis != null) {
            String message = "error : return %s ,";
            try {
                if (success) {
                    jedisPool.returnResource(jedis);
                    message = String.format(message, "resource");
                } else {
                    jedisPool.returnBrokenResource(jedis);
                    message = String.format(message, "broken resource");
                }
            } catch (Exception ex) {
                logger.error(message + ex);
            }
        }
    }

    public static byte[] serialize(Object object) {
        if (object == null)
            return null;
        ObjectOutputStream outputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            outputStream = new ObjectOutputStream(baos);
            outputStream.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            logger.error("Redis Util : serialize fail , " + e);
            return null;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static Object unserialize(byte[] bytes) {
        if (bytes == null)
            return null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            logger.error("Redis Util : unserialize fail , " + e);
            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

	/**
	 * 将删除指定字典从Redis缓存中删除
	 *
	 * @param rootKey 字典代码
	 * @author gaoyang
	 */
    public static void deleteAllByRootKey(Integer db, String rootKey) {
        if (rootKey == null || rootKey.isEmpty())
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            Set<String> keys = jedis.keys(rootKey+"*");//*是查询所有key，AJLB*,就是只查AJLB开头的key，参数应该是正则表达式
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
              String str = it.next();
              jedis.del(str);
            }
            
        } catch (Exception ex) {
            logger.error("Redis Util : delete fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
    }



    public static Jedis getJedis(Integer db) {
   	 JedisPool jedisPool = jedisPools.get(db);
   	 Jedis jedis = null;
        Boolean success = true;
        try {
            jedis = jedisPool.getResource();
            return jedis;
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail , " + ex);
            if (ex instanceof JedisException)
                success = false;
            return null;
        } finally {
            returnJedisResource(jedisPool, jedis, success);
        }
   }

    /**
     * 验证根据key 对应的hash中是否存在field字段
     * @author maoli
     * @param db 数据库
     * @param key 键
     * @param field hash中field字段
     * @return 是否存在
     */
   public static boolean hexists(Integer db, String key, String field) {
       if (db == null || key == null || field == null) {
           return false;
       }
       JedisPool jedisPool = jedisPools.get(db);
       boolean success = true;
       if (jedisPool != null) {
           Jedis jedis = null;
           try {
               jedis = jedisPool.getResource();
               if (jedis != null) {
                   return jedis.hexists(key, field);
               }
           } catch (Exception e) {
               logger.error("异常信息：", e);
               if (e instanceof JedisException) {
                   success = false;
               }
           } finally {
               returnJedisResource(jedisPool, jedis, success);
           }
       }
       return false;
   }


    /**
     * 验证key在库中是否存在
     * @author      zhangchunli
     * @param db    数据库
     * @param key   键
     * @return      是否存在，存在返回true，不存在返回false
     */
    public static boolean exists(Integer db, String key) {
        if (db == null || key == null ) {
            return false;
        }
        JedisPool jedisPool = jedisPools.get(db);
        boolean success = true;
        if (jedisPool != null) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                if (jedis != null) {
                    return jedis.exists(key);
                }
            } catch (Exception e) {
                logger.error("异常信息：", e);
                if (e instanceof JedisException) {
                    success = false;
                }
            } finally {
                returnJedisResource(jedisPool, jedis, success);
            }
        }
        return false;
    }

    /**
     * 获取jedis对象（外部调用使用完后需要手动调用returnJedisResource释放资源）
     * @author maoli
     * @param db 数据库
     * @return 需要归还的jedis对象
     */
    public static Jedis getNeedReturnJedis(Integer db) {
        JedisPool jedisPool = jedisPools.get(db);
        if (jedisPool != null) {
            return jedisPool.getResource();
        }
        return null;
    }

    /**
     * 释放Jedis资源
     * @author maoli
     * @param db 数据库
     * @param jedis Jedis资源
     * @param success 是否发生jedis异常（外部调用需要判断异常信息，参考hexists方法实现）
     */
    @SuppressWarnings("deprecation")
    public static void returnJedisResource(Integer db, Jedis jedis, boolean success) {
        JedisPool jedisPool = jedisPools.get(db);
        if (jedisPool != null && jedis != null) {
            String message = "error : return %s ,";
            try {
                if (success) {
                    jedisPool.returnResource(jedis);
                    message = String.format(message, "resource");
                } else {
                    jedisPool.returnBrokenResource(jedis);
                    message = String.format(message, "broken resource");
                }
            } catch (Exception ex) {
                logger.error("异常信息：" + message, ex);
            }
        }
    }

}
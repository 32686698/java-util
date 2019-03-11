package com.tian.test;

import redis.clients.jedis.Jedis;

/**
 * @ClassName RedisClient
 * @Description 创建redis连接，单例模式
 * @Author tianyasheng
 * @Date 2019/3/11 16:56
 **/
public class RedisClient {
    private RedisClient(){}

    private static class RedisClientInner{
        private static Jedis jedis = new Jedis("localhost");
    }

    public static Jedis getJedis(){
        return RedisClientInner.jedis;
    }
}

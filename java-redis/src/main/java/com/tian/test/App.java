package com.tian.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Jedis jedis = RedisClient.getJedis();
    @Test
    public void testCliect(){
        System.out.println( jedis.ping());
    }

    @Test
    public void testSetString(){
        jedis.set("aaa","bbbbbbbbbb");
    }

    @Test
    public void testSetList(){
        jedis.lpush("s-list","aaaaaaaa");
        jedis.lpush("s-list","bbbbbbbbbb");
        jedis.lpush("s-list","cccccccccccccc");
        jedis.lpush("s-list","dddddddd");

        List<String> list = jedis.lrange("s-list",0,4);
        for(String str : list){
            System.out.println(str);
        }
    }

    @Test
    public void testGetKeys(){
        Set<String> sets = jedis.keys("*");

        for(String str : sets){
            System.out.println(str);
        }
    }
}

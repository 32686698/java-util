package com.tian.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private static Jedis jedis = RedisUtil.getJedis(RedisUtil.DB_1);
    @Test
    public void testCliect(){
        System.out.println( jedis.ping());
        System.out.println(RedisUtil.getJedis(RedisUtil.DB_4).ping());
    }

    /**
     *@Author tianyasheng
     *@Description redis中存放字符串
     *@Date 2019/3/12 14:24 
     *@Param []
     *@Return void
     **/
    @Test
    public void testSetString(){
        jedis.set("aaa","bbbbbbbbbb");
    }

    /**
     *@Author tianyasheng
     *@Description redis中存放list
     *@Date 2019/3/12 14:25 
     *@Param []
     *@Return void
     **/
    @Test
    public void testSetList(){
        jedis.lpush("s-list","aaaaaaaa");
        jedis.lpush("s-list","bbbbbbbbbb");
        jedis.lpush("s-list","cccccccccccccc");
        jedis.lpush("s-list","dddddddd");
        jedis.lpush("s-list","aaaaaaaa");
        jedis.lpush("s-list","cccccccccccccc");

        List<String> list = jedis.lrange("s-list",0,4);
        for(String str : list){
            System.out.println(str);
        }
    }

    /**
     *@Author tianyasheng
     *@Description 获取所有的KEY
     *@Date 2019/3/12 14:25 
     *@Param []
     *@Return void
     **/
    @Test
    public void testGetKeys(){
        Set<String> sets = jedis.keys("*");

        for(String str : sets){
            System.out.println(str);
        }
    }

    /**
     *@Author tianyasheng
     *@Description redis保存对象
     *@Date 2019/3/12 14:48 
     *@Param []
     *@Return void
     **/
    @Test
    public void testPutObject() {
        UserModel user = new UserModel("11","tys","yasheng","18603815261");
        System.out.println(user.hashCode());
        jedis.set("cccccc".getBytes(),RedisUtil.serialize(user));
        byte[] bytes = jedis.get("cccccc".getBytes());
        UserModel user1 = (UserModel)RedisUtil.unserialize(bytes);
        System.out.println(user1.toString());
        System.out.println(user1.hashCode());
    }
    /**
     *@Author tianyasheng
     *@Description redis保存map
     *@Date 2019/3/12 14:48
     *@Param []
     *@Return void
     **/
    @Test
    public void testPutMap() {
        UserModel user = new UserModel("11","tys","yasheng","18603815261");
        jedis.hset("hset".getBytes(),"user".getBytes(),RedisUtil.serialize(user));
        jedis.hset("hset".getBytes(),"roleList".getBytes(), RedisUtil.serialize(Arrays.asList("111","222","333")));
        jedis.hset("hset","test","aaaaaaaaaaaaaa");
    }


    /**
     *@Author tianyasheng
     *@Description redis保存set集合
     *@Date 2019/3/12 15:05 
     *@Param []
     *@Return void
     **/
    @Test
    public void testPutSet() {
        //往集合中添加元素
        jedis.sadd("s-set","aaa","bbb","eee","bbb","ccc","zzz","eee");
        // 获取集合的长度
        System.out.println("Size of set is:" + jedis.scard("s-set"));
        Set<String> set = jedis.smembers("s-set");
        for(String str:set){
            System.out.println(str);
        }
    }

    /**
     *@Author tianyasheng
     *@Description redis保存有序的set集合
     *@Date 2019/3/12 15:05
     *@Param []
     *@Return void
     **/
    @Test
    public void testPutSortSet() {
        //往集合中添加元素
        jedis.zadd("s-sort",1,"aaa");
        jedis.zadd("s-sort",4,"ddd");
        jedis.zadd("s-sort",3,"ccc");
        jedis.zadd("s-sort",6,"eee");
        jedis.zadd("s-sort",2,"bbb");
        jedis.zadd("s-sort",2,"ffff");
        jedis.zadd("s-sort",7,"aaaa");
        //正序排列
        Set<String> mysort = jedis.zrange("s-sort",0,-1);
        System.out.println(mysort);
    }

}

class UserModel implements Serializable {
    private String id;
    private String name;
    private String password;
    private String phone;

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public UserModel(String id, String name, String password, String phone) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
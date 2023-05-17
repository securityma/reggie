package com.kobe.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.List;

@SpringBootTest
class ReggieApplicationTests {
    @Autowired
    RedisTemplate redisTemplate;
    @Test
    void contextLoads() {

    }
//    @Test
//    void testRedis(){
//        Jedis jedis=new Jedis("192.168.63.64",6379);
//        jedis.auth("123456");
//
//        jedis.set("213","xiaoming");
//        String s = jedis.get("213");
//        System.out.println(s);
//        for (String key : jedis.keys("*")) {
//            System.out.println(key);
//        }
//
//        jedis.close();
//    }
    @Test
    void redisTemplateTest(){
        try {
            HashOperations hashOperations=redisTemplate.opsForHash();

            hashOperations.put("002","name","xiaoming");
            hashOperations.put("002","age","12");
            hashOperations.put("002","add","bj");

            String name =(String) hashOperations.get("002", "name");
            System.out.println(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Test
    void ListTest()
    {
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll("as","ASD","sad");
        List as = listOperations.range("as", 0, -1);
        for (Object asitem:
             as) {
            System.out.println(asitem);

        }

    }

}

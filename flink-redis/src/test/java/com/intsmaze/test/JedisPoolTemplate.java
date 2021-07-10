package com.intsmaze.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisPoolTemplate {

    public static void main(String[] args) {
        ApplicationContext ct = new ClassPathXmlApplicationContext("spring-redis.xml");
        JedisPool pool = (JedisPool) ct.getBean("jedisPool");

        Jedis jedis = pool.getResource();// 从pool中获取资源
        try {
            jedis.set("k1", "v1");
            System.out.println(jedis.get("k1"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }

        for (int i = 0; i < 10; i++) {
            jedis = pool.getResource();
            // jedis.close(); // 去掉注释观察效果
            System.out.println("NumActive:" + pool.getNumActive());
            System.out.println("NumIdle:" + pool.getNumIdle());
            System.out.println("NumWaiters:" + pool.getNumWaiters());
        }
        pool.close();
        pool.destroy();


    }
}

package com.intsmaze.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;

public class ClusterRedisTemplate {

    public static void main(String[] args) throws IOException {

        ApplicationContext ct = new ClassPathXmlApplicationContext("spring-redis.xml");
        JedisCluster jc = (JedisCluster) ct.getBean("jedisCluster");

        System.out.println(jc.set("age", "20"));
        System.out.println(jc.set("sex", "男"));
        System.out.println(jc.get("name"));
        System.out.println(jc.get("age"));
        System.out.println(jc.get("sex"));
        jc.close();

    }

}

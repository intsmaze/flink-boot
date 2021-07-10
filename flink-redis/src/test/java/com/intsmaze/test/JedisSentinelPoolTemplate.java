package com.intsmaze.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.*;


public class JedisSentinelPoolTemplate {

    public static void main(String[] args) {

        ApplicationContext ct = new ClassPathXmlApplicationContext("spring-redis.xml");
        JedisSentinelPool jedisSentinelPool = (JedisSentinelPool) ct.getBean("jedisSentinelPool");


        for (int i = 0; i < 100; i++) {
            try {
                // 无论读写都是主服务器，从服务器并没有接收读请求，没有进行负载，仅仅是备份。即当主节点宕机了，从节点补上。
                String host = jedisSentinelPool.getCurrentHostMaster().getHost();
                int port = jedisSentinelPool.getCurrentHostMaster().getPort();
                System.out.println(host + ":" + port);

                Jedis jedis = jedisSentinelPool.getResource();
                // jedis.set("001", "ASDFG");
                System.out.println(jedis.get("001"));
                jedis.close(); // 关闭Redis Master服务

                host = jedisSentinelPool.getCurrentHostMaster().getHost();
                port = jedisSentinelPool.getCurrentHostMaster().getPort();
                System.out.println(host + ":" + port);

                jedis = jedisSentinelPool.getResource();
                System.out.println(jedis.get("001"));
                jedis.close();
                Thread.sleep(8000);
            } catch (Exception e) {
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
        jedisSentinelPool.close();
        jedisSentinelPool.destroy();
    }
}

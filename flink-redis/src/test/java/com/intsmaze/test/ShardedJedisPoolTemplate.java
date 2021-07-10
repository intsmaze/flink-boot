package com.intsmaze.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.*;

public class ShardedJedisPoolTemplate {
    public static void main(String[] args) {
        ApplicationContext ct = new ClassPathXmlApplicationContext("spring-redis.xml");
        ShardedJedisPool shardedJedisPool = (ShardedJedisPool) ct.getBean("shardedJedisPool");

        for (int i = 0; i < 10; i++) {
            ShardedJedis shardedJedis = shardedJedisPool.getResource();//发现Sharded的构造方法其实是在我们 jedisPool.getResource() 时就完成的,每次jedisPool.getResource() 都会初始化一次，所以通过这个功能完成了动态上下节点功能啪啪啪·
            String key = "shard" + i;
            shardedJedis.set(key, "v-" + i);
            System.out.println(shardedJedis.get(key));
            JedisShardInfo shardInfo = shardedJedis.getShardInfo(key);
            System.out.println("getHost:" + shardInfo.getHost());
            shardedJedis.close();
        }
        shardedJedisPool.close();
        shardedJedisPool.destroy();
    }
}

package com.intsmaze.flink.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.transform.BuiltinRichFlatMapFunction;
import com.intsmaze.flink.lock.DistributedLock;
import com.intsmaze.flink.lock.LockFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2021/07/10 18:33
 */
public class RedisFlatMap extends BuiltinRichFlatMapFunction {

    private Logger logger = LoggerFactory.getLogger(RedisFlatMap.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private JedisPool jedisPool;

    private LockFactory lockFactory;

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2021/07/10 18:33
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        jedisPool = beanFactory.getBean(JedisPool.class);
        lockFactory = beanFactory.getBean(LockFactory.class);
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2021/07/10 18:33
     */
    @Override
    public String execute(String message) {

        FlowData flowData = gson.fromJson(message, new TypeToken<FlowData>() {
        }.getType());

        DistributedLock distributedLock = lockFactory.newLock("lock:redis:" + flowData.getBillNumber());
        distributedLock.lock();
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(StringUtils.join("intsmaze", flowData.getBarcode()), flowData.getBarcode() + System.currentTimeMillis());
            String result = jedis.get(StringUtils.join("intsmaze", flowData.getBarcode()));
            distributedLock.unlock();
            return result;
        } catch (Exception e) {
            jedis.close();
            e.printStackTrace();
        } finally {
            if (distributedLock != null) {
                logger.info("释放释放分布式锁。。。。。。。。。。。。。。。。。。。。");
                distributedLock.unlock();
            }
        }
        return null;
    }
}

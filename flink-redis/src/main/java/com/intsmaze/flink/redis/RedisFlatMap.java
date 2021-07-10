package com.intsmaze.flink.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.transform.CommonFunction;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
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
public class RedisFlatMap extends CommonFunction {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private JedisPool jedisPool;

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

        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(StringUtils.join("intsmaze", flowData.getBarcode()), flowData.getBarcode() + System.currentTimeMillis());
            String result = jedis.get(StringUtils.join("intsmaze", flowData.getBarcode()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }
}

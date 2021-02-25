package com.intsmaze.drpc;

import com.intsmaze.RedisTmp;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;


/**
 * @author ：intsmaze
 * @date ：Created in 2021/1/27 16:56
 * @description： https://www.cnblogs.com/intsmaze/
 * @modified By：
 */
public class DprcDataSink implements SinkFunction<String> {

    @Override
    public void invoke(String value, Context context) throws Exception {
        System.out.println("sink -----------"+value);
        String[] split = value.split(":");
        RedisTmp.RedisData.put(split[1],"处理结束："+split[0]);
    }
}

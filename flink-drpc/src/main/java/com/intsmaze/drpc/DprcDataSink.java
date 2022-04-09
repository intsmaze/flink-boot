package com.intsmaze.drpc;

import com.google.gson.Gson;
import com.intsmaze.bean.DrpcBean;
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
        Gson gson=new Gson();
        DrpcBean drpcBean = gson.fromJson(value, DrpcBean.class);
        RedisTmp.RedisData.put(drpcBean.getUuid(),drpcBean.getData());
    }
}

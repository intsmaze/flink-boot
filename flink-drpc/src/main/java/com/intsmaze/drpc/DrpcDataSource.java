package com.intsmaze.drpc;

import com.google.gson.Gson;
import com.intsmaze.RedisTmp;
import com.intsmaze.bean.DrpcBean;
import com.intsmaze.dubbo.provider.DubboService;
import com.intsmaze.flink.base.env.BeanFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class DrpcDataSource extends RichParallelSourceFunction<String> implements DubboService {


    protected ApplicationContext beanFactory;

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ExecutionConfig.GlobalJobParameters globalJobParameters = getRuntimeContext()
                .getExecutionConfig().getGlobalJobParameters();
        beanFactory = BeanFactory.getDubboBeanFactory((Configuration) globalJobParameters);
    }


    public static LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue(10);


    /**
     * 负责对外发布服务
     *
     * @param value
     * @return
     * @throws InterruptedException
     */
    @Override
    public String flinkDealMess(String value) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "--flink source--" + value + "  size:" + DrpcDataSource.linkedBlockingQueue.size());
        UUID uuid = UUID.randomUUID();
        try {
            DrpcDataSource.linkedBlockingQueue.put(new Gson().toJson(new DrpcBean(value, uuid.toString())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result = "超时消息";
        RedisTmp.RedisData.put(uuid.toString(), "");

        //下游算子处理完的结果存储到RedisData中，然后服务从这里获取结果返回给请求
        for (int i = 0; i < 80; i++) {
            result = RedisTmp.RedisData.get(uuid.toString());
            if (StringUtils.isNotBlank(result)) {
                break;
            }
            Thread.sleep(10);
        }

        return "flink dubbo provider return mess: " + result;
    }


    @Override
    public void run(SourceContext<String> sourceContext) throws Exception {
        while (true) {
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + " linkedBlockingQueue size:" + linkedBlockingQueue.size());
            //获取服务接收到的消息然后发送给下游算子进行处理
            String drpcBean = linkedBlockingQueue.take();
            sourceContext.collect(drpcBean);
        }
    }

    @Override
    public void cancel() {

    }


}

package com.intsmaze.drpc;

import com.intsmaze.RedisTmp;
import com.intsmaze.dubbo.provider.DubboService;
import com.intsmaze.flink.base.transform.CommonDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;

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
public class DrpcDataSource extends CommonDataSource implements DubboService, CheckpointedFunction {

    DubboService dubboService;

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
        dubboService = beanFactory.getBean(DubboService.class);
    }

    @Override
    public String sayHello(String name) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "--11111111111--" + name + "  size:" + linkedBlockingQueue.size());
        UUID uuid = UUID.randomUUID();
        bufferedElements.add(uuid.toString());
        try {
            linkedBlockingQueue.put(name + ":" + uuid.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String s="";
        RedisTmp.RedisData.put(uuid.toString(), "");
        for (int i = 0; i < 80; i++) {
            s = RedisTmp.RedisData.get(uuid.toString());
            if (StringUtils.isNotBlank(s)) {
                break;
            }
            Thread.sleep(10);
        }
        if (StringUtils.isBlank(s)) {
            s="超时消息";
        }

        bufferedElements.remove(uuid.toString());//应该是消费组接收成功消息后再进行remove操作，所以服务提供者响应后应该要回调一下。
        return "dubbo provider return mess: " + s;
    }


    public static LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue(10);

    @Override
    public void run(SourceContext<String> sourceContext) throws Exception {
        while (true) {
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + " linkedBlockingQueue size:" + linkedBlockingQueue.size());
            String poll = linkedBlockingQueue.take();
            if (StringUtils.isBlank(poll)) {
                sourceContext.collect("null");
            } else {
                sourceContext.collect(poll);
            }
        }
    }


    private transient ListState<String> checkpointedState;

    private LinkedList<String> bufferedElements= new LinkedList<>();;


    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public String sendMess() {
        return null;
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        checkpointedState.update(bufferedElements);
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<String> descriptor =
                new ListStateDescriptor<String>(
                        "ListState",
                        TypeInformation.of(new TypeHint<String>() {
                        }));
        checkpointedState = context.getOperatorStateStore().getListState(descriptor);
        if (context.isRestored()) {
            for (String element : checkpointedState.get()) {
                bufferedElements.offer(element);
            }
        }
    }
}

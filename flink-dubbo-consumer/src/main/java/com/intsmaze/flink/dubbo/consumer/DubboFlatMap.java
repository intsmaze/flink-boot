package com.intsmaze.flink.dubbo.consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.transform.BuiltinRichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import com.intsmaze.dubbo.provider.DubboService;


/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 20202/10/15 18:33
 */
public class DubboFlatMap extends BuiltinRichFlatMapFunction {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private DubboService dubboService;

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2022/10/15 18:33
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        dubboService = beanFactory.getBean(DubboService.class);
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2022/10/15 18:33
     */
    @Override
    public String execute(String message) {

        FlowData flowData = gson.fromJson(message, new TypeToken<FlowData>() {
        }.getType());

        return dubboService.flinkDealMess(gson.toJson(flowData));
    }
}

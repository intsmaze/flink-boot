package com.intsmaze.flink.retry.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.bean.SourceData;
import com.intsmaze.flink.base.transform.CommonFunction;
import com.intsmaze.flink.retry.DataServiceWithRetry;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;

import java.util.LinkedList;
import java.util.UUID;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class RetryFlatMap extends CommonFunction {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    private DataServiceWithRetry dataServiceWithRetry;

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public void open(Configuration parameters) {
        super.open(parameters);
        dataServiceWithRetry = beanFactory.getBean(DataServiceWithRetry.class);
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public String execute(String message) throws Exception {

        FlowData flowData = gson.fromJson(message, new TypeToken<FlowData>() {
        }.getType());

        String flowUUID = dataServiceWithRetry.findUUID(flowData);
        if (StringUtils.isBlank(flowUUID)) {
            flowUUID = UUID.randomUUID().toString();
            flowData.setUuid(flowUUID);
            dataServiceWithRetry.insertFlow(flowData);
        }
        return gson.toJson(flowData);
    }
}

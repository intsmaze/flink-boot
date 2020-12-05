package com.intsmaze.flink.client.task.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.bean.SourceData;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.LinkedList;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class GsonFlatMap extends RichFlatMapFunction<String, Tuple2<SourceData, FlowData>> {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 记录接收的数据的数量
     */
    private IntCounter numLines = new IntCounter();

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
        getRuntimeContext().addAccumulator("num-GsonFlatMap", this.numLines);

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
    public void flatMap(String value, Collector<Tuple2<SourceData, FlowData>> out) {

        LinkedList<SourceData> dataLinkedList = gson.fromJson(value, new TypeToken<LinkedList<SourceData>>() {
        }.getType());

        LinkedList<FlowData> flowDataLinkedList = gson.fromJson(value, new TypeToken<LinkedList<FlowData>>() {
        }.getType());

        for (int i = 0; i < dataLinkedList.size(); i++) {
            this.numLines.add(1);
            SourceData sourceData = dataLinkedList.get(i);
            sourceData.setThreadName(Thread.currentThread().getName());
            FlowData flowData = flowDataLinkedList.get(i);
            out.collect(Tuple2.of(sourceData, flowData));
        }

    }
}

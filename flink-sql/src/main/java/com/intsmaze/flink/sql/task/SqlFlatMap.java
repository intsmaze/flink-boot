package com.intsmaze.flink.sql.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intsmaze.flink.base.bean.SourceData;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class SqlFlatMap extends RichFlatMapFunction<String, SourceData> {

    private static final Logger LOG = LoggerFactory.getLogger(SqlFlatMap.class);

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
    public void open(Configuration parameters) {
        getRuntimeContext().addAccumulator("num-SqlFlatMap", this.numLines);
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
    public void flatMap(String value, Collector<SourceData> out) {
        try {
            numLines.add(1);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            SourceData sourceData = gson.fromJson(value, SourceData.class);
            out.collect(sourceData);
        } catch (Exception e) {
            LOG.error("",e);
        }
    }


}

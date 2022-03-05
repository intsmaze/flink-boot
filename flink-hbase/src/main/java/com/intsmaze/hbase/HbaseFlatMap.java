package com.intsmaze.hbase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.transform.BuiltinRichFlatMapFunction;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.configuration.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;


/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class HbaseFlatMap extends BuiltinRichFlatMapFunction {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private HbaseHelper hbaseHelper;

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
        ExecutionConfig executionConfig = getRuntimeContext().getExecutionConfig();
        ExecutionConfig.GlobalJobParameters globalJobParameters = executionConfig.getGlobalJobParameters();
        Configuration configuration = (Configuration) globalJobParameters;
        String string = configuration.getString("hbase.zookeeper.quorum", "");
        org.apache.hadoop.conf.Configuration hbaseConfiguration = HBaseConfiguration.create();
        hbaseConfiguration.set("hbase.zookeeper.quorum", string);
        Connection hbaseConnection = ConnectionFactory.createConnection(hbaseConfiguration);
        hbaseHelper = new HbaseHelper(hbaseConnection);
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
    public String execute(String message) {
        FlowData flowData = gson.fromJson(message, new TypeToken<FlowData>() {
        }.getType());
        try {
            String[] columnNames = {"subTestItem", "billNumber"};
            String[] values = {flowData.getSubTestItem(), flowData.getBillNumber()};
            hbaseHelper.putHbase("hbase_intsmaze", flowData.getUuid(), "f1", columnNames, values);
            return hbaseHelper.getHbase("hbase_intsmaze", flowData.getUuid());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return "处理异常" + flowData;
        }
    }
}

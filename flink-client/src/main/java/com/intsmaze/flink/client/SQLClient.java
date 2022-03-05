//package com.intsmaze.flink.client;
//
//import com.intsmaze.flink.base.bean.SourceData;
//import com.intsmaze.flink.base.env.BaseFlink;
//import com.intsmaze.flink.sql.XmlUtils;
//import com.intsmaze.flink.sql.task.SqlFlatMap;
//import org.apache.flink.api.java.utils.ParameterTool;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.table.api.Table;
//import org.apache.flink.types.Row;
//
//import java.util.Map;
//
//import static org.apache.flink.table.api.Expressions.$;
//
//
///**
// * github地址: https://github.com/intsmaze
// * 博客地址：https://www.cnblogs.com/intsmaze/
// * 出版书籍《深入理解Flink核心设计与实践原理》
// *
// * @auther: intsmaze(刘洋)
// * @date: 2020/10/15 18:33
// */
//public class SQLClient extends BaseFlink {
//
//
//    private Map<String, String> xmlForSql;
//
//    public SQLClient(String[] paths) throws Exception {
//        this.xmlForSql = XmlUtils.readXMLForSql(paths);
//    }
//
//
//    /**
//     * 本地启动参数  -isLocal local
//     * 集群启动参数  -isIncremental isIncremental
//     *
//     * @param args
//     * @throws Exception
//     */
//    public static void main(String[] args) throws Exception {
//        SQLClient topo = new SQLClient(new String[]{"etl.sql"});
//        topo.run(ParameterTool.fromArgs(args));
//    }
//
//    @Override
//    public String getJobName() {
//        return "SQLClient";
//    }
//
//    @Override
//    public String getConfigName() {
//        return "topology-base.xml";
//    }
//
//    @Override
//    public String getPropertiesName() {
//        return "config.properties";
//    }
//
//    @Override
//    public void createTopology(StreamExecutionEnvironment builder) {
//
//        System.out.println(properties.getProperty("sqlTopic"));
//        DataStream<String> inputDataStrem = getKafkaSpout(properties.getProperty("sqlTopic").trim());
//
//        DataStream<SourceData> dataStream = inputDataStrem.flatMap(new SqlFlatMap());
//
//        //额外声明一个字段作为时间属性，这个额外字段必须位于schema的最后定义
//        tableEnv.createTemporaryView("sourceData", dataStream, $("flag"),$("uuid"),$("testTime"),$("seqId")
//                ,$("stepNumber"),$("stepType"),$("cycleNumber"),$("batchNumber"),$("flowId"),$("threadName")
//                );
//
//        Table resultNew = tableEnv.sqlQuery(xmlForSql.get("sql_temp"));
//
//        DataStream<Row> resultBeanDataStream = tableEnv.toAppendStream(resultNew, Row.class);
//
//        resultBeanDataStream.print("校验后的数据");
//    }
//
//}
//

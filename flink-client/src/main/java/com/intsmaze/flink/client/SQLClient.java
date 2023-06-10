package com.intsmaze.flink.client;

import com.intsmaze.flink.base.env.BaseFlink;
import com.intsmaze.flink.sql.XmlUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

import static org.apache.flink.table.api.Expressions.$;


/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class SQLClient extends BaseFlink {


    private Map<String, String> xmlForSql;

    public SQLClient(String[] paths) throws Exception {
        this.xmlForSql = XmlUtils.readXMLForSql(paths);
    }


    /**
     * 本地启动参数  -isLocal local
     * 集群启动参数  -isIncremental isIncremental
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SQLClient topo = new SQLClient(new String[]{"etl.sql"});
        topo.run(ParameterTool.fromArgs(args));
    }

    @Override
    public String getJobName() {
        return "SQLClient";
    }

    @Override
    public String getConfigName() {
        return "topology-base.xml";
    }

    @Override
    public String getPropertiesName() {
        return "config.properties";
    }

    @Override
    public void createTopology(StreamExecutionEnvironment builder) {

        tableEnv.executeSql(xmlForSql.get("source_table"));
        tableEnv.executeSql(xmlForSql.get("sink_table"));
        tableEnv.executeSql(xmlForSql.get("agg_view"));
        tableEnv.executeSql(xmlForSql.get("insert_table"));

    }

}


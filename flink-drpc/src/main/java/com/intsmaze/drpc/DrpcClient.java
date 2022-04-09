package com.intsmaze.drpc;

import com.google.gson.Gson;
import com.intsmaze.RedisTmp;
import com.intsmaze.bean.DrpcBean;
import com.intsmaze.flink.base.env.BaseFlink;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class DrpcClient extends BaseFlink {

    /**
     * 本地启动参数  -isLocal local
     * 集群启动参数  -isIncremental isIncremental
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DrpcClient topo = new DrpcClient();
        topo.run(ParameterTool.fromArgs(args));
    }

    @Override
    public String getJobName() {
        return "DrpcClient";
    }

    @Override
    public String getConfigName() {
        return "topology-base.xml";
    }

    @Override
    public String getDubboConfigName() {
        return "dubbo-provider.xml";
    }

    @Override
    public String getPropertiesName() {
        return "config.properties";
    }

    @Override
    public void createTopology(StreamExecutionEnvironment builder) {
        builder.setParallelism(1);
        env.addSource(new DrpcDataSource())
                .map(new MapFunction<String, String>() {
                    @Override
                    public String map(String value) throws Exception {
                        DrpcBean drpcBean = new Gson().fromJson(value, DrpcBean.class);
                        drpcBean.setData("flink处理结束后的结果:"+drpcBean.getData());
                        return new Gson().toJson(drpcBean);
                    }
                }).addSink(new DprcDataSink());
    }

}


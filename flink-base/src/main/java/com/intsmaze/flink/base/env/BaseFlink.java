package com.intsmaze.flink.base.env;

import com.intsmaze.flink.base.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.PredefinedOptions;
import org.apache.flink.contrib.streaming.state.RocksDBOptions;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.util.Preconditions;
import org.joda.time.DateTime;
import org.rocksdb.DBOptions;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public abstract class BaseFlink {

    protected Configuration config = new Configuration();

    protected BeanFactory beanFactory;

    protected String configFile;

    protected EnvironmentSettings settings;

    protected StreamExecutionEnvironment env;

    protected StreamTableEnvironment tableEnv;

    protected Properties properties;

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public void init(ParameterTool params) throws IOException {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        tableEnv = StreamTableEnvironment.create(env, settings);


        this.properties = PropertiesUtils.getProperties(getPropertiesName());
        Configuration configuration = new Configuration();
        configuration.addAllToProperties(properties);
        env.getConfig().setGlobalJobParameters(configuration);

        String parallelism = params.get("parallelism");
        if (StringUtils.isNotBlank(parallelism)) {
            env.setParallelism(Integer.valueOf(parallelism));
        }

        String restartStrategy = params.get("restartStrategy");
        if ("fixedDelayRestart".equals(restartStrategy)) {
            env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
                    3,
                    Time.of(60, TimeUnit.SECONDS)
            ));
        } else if ("noRestart".equals(restartStrategy)) {
            env.setRestartStrategy(RestartStrategies.noRestart());
        } else if ("fallBackRestart".equals(restartStrategy)) {
            env.setRestartStrategy(RestartStrategies.fallBackRestart());
        } else {
            env.setRestartStrategy(RestartStrategies.failureRateRestart(
                    3,
                    Time.of(5, TimeUnit.MINUTES),
                    Time.of(60, TimeUnit.SECONDS)
            ));
        }

        String isLocal = params.get("isLocal");
        if (StringUtils.isBlank(isLocal)) {
            String isIncremental = params.get("isIncremental");
            Preconditions.checkNotNull(isIncremental, "isIncremental is null");
            RocksDBStateBackend stateBackend;
            String hadoopIp = properties.getProperty("hadoopIp");
            if ("isIncremental".equals(isIncremental)) {
                //如果本地调试，必须指定hdfs的端口信息，且要依赖hadoop包，如果集群执行，flink与hdfs在同一集群，那么可以不指定hdfs端口信息，也不用将hadoop打进jar包。
                stateBackend = new RocksDBStateBackend("hdfs:///home/intsmaze/flink/" + getJobName(), true);
                env.setStateBackend(stateBackend);
            } else if ("full".equals(isIncremental)) {
                stateBackend = new RocksDBStateBackend("hdfs://" + hadoopIp + "/home/intsmaze/flink/" + getJobName(), false);
                env.setStateBackend(stateBackend);
            }

            env.enableCheckpointing(5000);
            env.getCheckpointConfig().setCheckpointTimeout(30000);
            env.getCheckpointConfig().setMinPauseBetweenCheckpoints(5000);
            env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
            env.getCheckpointConfig().setFailOnCheckpointingErrors(false);
            env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        }
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    protected void setupConfig() {

        this.configFile = getConfigName();
        this.beanFactory = new BeanFactory(configFile);

        for (String key : this.properties.stringPropertyNames()) {
            this.config.setString(key, this.properties.getProperty(key));
        }
        this.config.setString(BeanFactory.SPRING_BEAN_FACTORY_XML, beanFactory.getXml());
        this.config.setString(BeanFactory.SPRING_BEAN_FACTORY_NAME, this.configFile);
        env.getConfig().setGlobalJobParameters(this.config);

    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    protected DataStream<String> getKafkaSpout(String topic) {

        String bootstrapServers = properties.getProperty("bootstrap.servers", "");
        String port = properties.getProperty("kafka.offset.Port", "");

        String id = StringUtils.join(getJobName(), "-", topic);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", bootstrapServers + ":" + port);
        properties.setProperty("group.id", id);
        System.out.println("-------------->>>>>>>>>>>>>>>>>> consumer name is :" + id);
        FlinkKafkaConsumer<String> stringFlinkKafkaConsumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), properties);
        return env.addSource(stringFlinkKafkaConsumer);
    }


    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public abstract String getJobName();

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public abstract String getConfigName();

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public abstract String getPropertiesName();

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public abstract void createTopology(StreamExecutionEnvironment builder) throws IOException;

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public void run(ParameterTool params) throws Exception {

        init(params);
        setupConfig();
        createTopology(env);

        String topoName = StringUtils.join(getJobName(), "-", new DateTime().toString("yyyyMMdd-HHmmss"));
        env.execute(topoName);
    }

}

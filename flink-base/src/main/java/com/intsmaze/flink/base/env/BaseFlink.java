package com.intsmaze.flink.base.env;

import com.google.common.base.Preconditions;
import com.intsmaze.flink.base.thread.SwitchThread;
import com.intsmaze.flink.base.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Properties;
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
        String isLocal = params.get("isLocal");
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        tableEnv = TableEnvironment.getTableEnvironment(env);

        this.properties = PropertiesUtils.getProperties(getPropertiesName());

        String parallelism = properties.getProperty("parallelism");
        if (StringUtils.isNotBlank(parallelism)) {
            env.setParallelism(Integer.valueOf(parallelism));
        }

        if (StringUtils.isBlank(isLocal)) {
            env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
            String isIncremental = params.get("isIncremental");
            Preconditions.checkNotNull(isIncremental, "isIncremental is null");
            StateBackend stateBackend;
            String hadoopIp = properties.getProperty("hadoopIp");
            if ("isIncremental".equals(isIncremental)) {
                //如果本地调试，必须指定hdfs的端口信息，且要依赖hadoop包，如果集群执行，flink与hdfs在同一集群，那么可以不指定hdfs端口信息，也不用将hadoop打进jar包。
//           stateBackend=new RocksDBStateBackend("hdfs://centos-Reall-131:9000/home/intsmaze/flink/", true);
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
    protected void setupConfig() throws IOException {

        this.configFile = getConfigName();
        this.beanFactory = new BeanFactory(configFile);

//        Map<String, String> stormConfig = beanFactory.getBean("flinkConfig", Map.class);
//        Preconditions.checkNotNull(stormConfig);
//
//        Iterator<Map.Entry<String, String>> iterator = stormConfig.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, String> next = iterator.next();
//            String key = next.getKey();
//            String value = next.getValue();
//            config.setString(key, value);
//        }
        config.setString(BeanFactory.SPRING_BEAN_FACTORY_XML, beanFactory.getXml());
        config.setString(BeanFactory.SPRING_BEAN_FACTORY_NAME, this.configFile);
        env.getConfig().setGlobalJobParameters(config);

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
        return env.addSource(stringFlinkKafkaConsumer).map(new InnerMap());
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

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    class InnerMap extends RichMapFunction<String, String> {

        public Logger log = LoggerFactory.getLogger(BeanFactory.class);

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
            ExecutionConfig.GlobalJobParameters globalJobParameters = getRuntimeContext()
                    .getExecutionConfig().getGlobalJobParameters();
            ApplicationContext beanFactory = BeanFactory.getBeanFactory((Configuration) globalJobParameters);
            beanFactory.getBean(SwitchThread.class);
            System.out.println(SwitchThread.activityConfigs.getOrDefault(SwitchThread.isSleep, "no"));
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
        public String map(String value) {
            try {
                String isSleep = (String) SwitchThread.activityConfigs.getOrDefault(SwitchThread.isSleep, "no");
                if (StringUtils.equals(isSleep, "yes")) {
                    log.error("每运行一次,休眠30m");
                    TimeUnit.SECONDS.sleep(30);
                }
            } catch (Exception e) {
                log.error("", e);
            }
            return value;
        }
    }

}

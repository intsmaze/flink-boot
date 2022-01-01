
# 懒松鼠Flink-Boot（稳定版请切分支拉取）
#### [懒松鼠Flink-Boot 脚手架由《深入理解Flink核心设计与实践原理》作者开发。](https://github.com/intsmaze/flink-book)
![image](https://github.com/intsmaze/flink-boot/blob/master/fm.png)
##### 该脚手架屏蔽掉组装Flink API细节，让Flink全面拥抱Spring生态体系，使得开发者能以传统Java WEB模式的开发方式开发出具备分布式计算能力的流处理程序,懒松鼠让跨界变得更加简单。
##### 懒松鼠旨在让开发者以更底上手成本（不需要理解分布式计算的理论知识和Flink框架的细节）便可以快速编写业务代码实现。
##### 为了进一步提升开发者使用该脚手架开发大型项目的敏捷的程度，该脚手架工程默认集成Spring框架进行Bean管理，同时将微服务以及WEB开发领域中经常用到的框架集成进来，进一步提升开发速度。比如集成:
* 集成Jbcp-template对Mysql,Oracle,SQLServer等关系型数据库的快速访问。
* 集成Hibernate Validator框架进行参数校验。
* 集成Spring Retry框架进行重试标志。
* 集成Mybatis框架,提高对关系型数据库增，删，改，查的开发速度。
* 集成Spring Cache框架,实现注解式定义方法缓存。
* 集成Dubbo 框架,实现Dubbo Consumer调用Spring Cloud中的服务接口。
* 集成redis客户端，实现多种集群模式消费者，比如哨兵模式，集群模式等。
* 提供DPRC接口模型，以接口化的方式对外部服务提供接口调用，赋能fink计算能力
* ......

## 技术选项和集成情况
技术 | 名称 | 状态 |
----|------|----
Spring Framework | 容器  | 已集成 
Spring 基于XML方式配置Bean | 装配Bean  | 已集成 
Spring 基于注解方式配置Bean | 装配Bean  | 已集成
Spring 基于注解声明方法重试机制 | Retry注解  | 已集成 
Spring 基于注解声明方法缓存 | Cache注解  | 已集成 
Hibernate Validator | 校验框架  | 已集成
Dubbole消费者 | 服务消费者  | 已集成 
Dubbo服务提供者 | 分布式服务框架  | 已集成 
DRPC接口 | flink对外暴露dprc接口提供接口服务  | 已集成 
Apollo配置中心 | 携程阿波罗配置中心  | 已集成 
Druid | 数据库连接池  | 已集成 
MyBatis | ORM框架  | 已集成 
Kafka | 消息队列  | 已集成
Redis | 分布式缓存数据库  | 已集成 
分布式锁 | 基于Redis的分布式锁实现  | 已集成 
HDFS | 分布式文件系统  | 已集成
Hbase | No-Sql数据库 | 已集成 
groovy | 规则表达式  | 已集成 
Junit | 单元测试  | 已集成 
MDC | 分布式日志链路追踪  | 已集成 
Mybatis-Plus | MyBatis扩展包  | 进行中 
PageHelper | MyBatis物理分页插件  | 进行中 
ZooKeeper | 分布式协调服务  | 进行中
Solr & Elasticsearch | 分布式全文搜索引擎  | 进行中
Ehcache | 进程内缓存框架  | 进行中 
sequence | 分布式高效ID生产  | 进行中 
Spring eurake消费者 | 服务消费者  | 进行中 
Spring Config配置中心 | Spring Cloud Config配置中心  | 进行中 

## 诞生由来
* 本人一直认为，Flink框架，在某些场景完全可以替代Spring微服务框架，但是很多人仍认为它只能用在OLAP场景，对批处理场景的扩展（批流一体不做讨论）。
* 作为一个深耕流计算领域，同时具备java分布式开发能力的开发者，我一直认为一个Flink也好，Spark Streaming也好，Storm也好，流计算开发工程师必须具备深厚的Java功底，不然无法解决高并发的经验，毕竟和离线计算相比，它的失败重跑的代价太大，同时要和很多第三方系统进行交互，比如作为服务的消费组调用Eurake中的服务提供者，存储数据到关系型数据库，这又涉及到事务等理论等等。
* 目前市场的普遍情况就是统计一些PU,VU指标等，更或者仅仅是实时ETL，面向SQL编程。很多流计算开发者对Flink框架的特性头头是道，但是基本的Java功底却很薄弱。
* **开发DRPC组件接口，该组件基于Dubbo服务注册者来注册服务供服务消费组进行消费，做到真正的同步响应。**

## [《深入理解Flink核心设计与实践原理》京东商城购买链接](https://item.jd.com/12765369.html)


<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=6 orderedList=false} -->

<!-- code_chunk_output -->

- [你可能面临如下苦恼](#你可能面临如下苦恼)
  - [接口缓存](#接口缓存)
  - [重试机制](#重试机制)
  - [Bean校验](#Bean校验)
  - [与市面上其他集成Spring生态的优势](#与市面上其他集成Spring生态的优势)
  - [等等](#等等......)
- [组织结构 (必看 :+1:)](#组织结构)
- [技术选项和集成情况](#技术选项和集成情况)
- [快速开始](#快速开始)
  - [核心基础工程](核心基础工程)
  - [Spring容器](Spring容器)
  - [启动类示例](#启动类示例)
  - [数据源](#数据源)
  - [业务逻辑实现](#业务逻辑实现)
  - [集群/本地运行](#集群/本地运行)
- [演示地址 (必看 :+1:)](#演示地址)
- [其他](#其他)
  - [待办](#待办)
  - [联系我](#联系我)
  - [捐赠支持](#捐赠支持)
  - [Contributor](#贡献者)

<!-- /code_chunk_output -->

## 你可能面临如下苦恼：

1. 开发的Flink流处理应用程序，业务逻辑全部写在Flink的操作符中，代码无法复用，无法分层
2. 要是有一天它可以像开发Spring Boot程序那样可以优雅的分层，优雅的装配Bean，不需要自己new对象好了
3. 可以使用各种Spring生态的框架，一些琐碎的逻辑不再硬编码到代码中。

### 接口缓存

**你的现状**

```
static Map<String,String> cache=new HashMap<String,String>();

public String findUUID(FlowData flowData) {
    String value=cache.get(flowData.getSubTestItem());
    if(value==null)
    {
        String uuid=userMapper.findUUID(flowData);
        cache.put(uuid,value);
        return uuid;
    }
    return value;
}
```

**你想要的是这样**

```
@Cacheable(value = "FlowData.findUUID", key = "#flowData.subTestItem")
public String findUUID(FlowData flowData) {
    return userMapper.findUUID(flowData);
}
```

### 重试机制

**你的现状**

```java
public void insertFlow(FlowData flowData) {
    try{
        userMapper.insertFlow(flowData);
      }Cache(Exception e)
      {
         Thread.sleep(10000);
         userMapper.insertFlow(flowData);
      }
}
```

**你想要的是这样**

```java
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 1.5))
    @Override
    public void insertFlow(FlowData flowData) {
        userMapper.insertFlow(flowData);
    }
```



### Bean校验

**你的现状**

```
if(flowData.getSubTestItem().length()<2&&flowData.getSubTestItem().length()>7)
{
    return null;
}
if(flowData.getBillNumber()==null)
{
    return null;
}
```

**你想要的是这样**

```
Map<String, StringBuffer> validate = ValidatorUtil.validate(flowData);
if (validate != null) {
    System.out.println(validate);
    return null;
}

public class FlowData {

    private String uuid;
    //声明该参数的校验规则字符串长度必须在7到20之间
    @Size(min = 7, max = 20, message = "长度必须在{min}-{max}之间")
    private String subTestItem;
    //声明该参数的校验规则字符串不能为空
    @NotBlank(message = "billNumber不能为空")
    private String billNumber;
}
```
### 与市面上其他集成Spring生态的优势
* 不需要将集成Spring的程序JAR包放置在Flink集群每个节点的lib包下面，比如这个例子：https://blog.csdn.net/javajxz008/article/details/94656679
### 等等......


## 组织结构

``` lua
Flink-Boot
├── Flink-Base -- Flink-Boot工程基础模块/Engineering basic module
├── Flink-Client -- Flink-Boot 客户端模块/Client module
├── flink-annotation -- 注解生效模块/Annotation effective module
├── flink-mybatis -- mybatis orm模块/mybatis orm module
├── flink-retry -- 注解重试机制模块/Annotation retry mechanism module
├── flink-validate -- 校验模块/validate module
├── flink-sql -- Flink SQL解耦至XML配置模块/SQL decoupling to XML configuration module
├── flink-cache-annotation -- 接口缓冲模块/Interface buffer module
├── flink-dubbo-comsumer -- Dubbo 消费组模块/Dubbo comsumer module
├── flink-hbase -- Hbase 模块/Hbase module
├── flink-redis -- redis 模块/redis module
├── flink-drpc -- Drpc 模块/DRPC module
├── flink-other-service -- 组件原生运行模块
├── flink-junit -- 单元测试模块/Unit test module
├── flink-apollo -- 阿波罗配置客户端模块/Apollo configuration client module
```



## 快速开始

下面是集成Spring生态的基础手册，通过目录中的[联系我](#联系我)标题可以找到我已获取更详细的开发手册，当然技术过硬自己摸索也只需3小时即可上手所有模块。

### 核心基础工程

* flink-base :基础工程，封装了开发Flink工程的必须参数，同时集成Spring容器，为后续集成Spring各类框架提供了支撑。
  1. 可以在本地开发环境和Flink集群运行环境中随意切换。
  2. 可以在增量检查点和全量检查点之间随意切换。
  3. 内置使用HDFS作为检查点的持久存储介质。
  4. 默认使用Kafka作为数据源
  5. 内置实现了任务的暂停机制-达到任务仍在运行但不再接收Kafka数据源中的数据，代替了停止任务后再重新部署任务这一繁琐流程。
 * flink-client：业务工程，该工程依赖flink-base工程，开发任务在该工程中进行业务逻辑的开发。

### Spring容器

该容器模式配置了JdbcTemplate实例，数据库连接池采用Druid，在业务方法中只需要获取容器中的JdbcTemplate实例便可以快速与关系型数据库进行交互,dataService实例封装了一些访问数据库表的方法。

#### topology-base.xml

```
<beans ......
       default-lazy-init="true" default-init-method="init">

    <context:property-placeholder location="classpath:config.properties"/>

    <bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url"
                  value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.user}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
    
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <constructor-arg ref="druidDataSource"></constructor-arg>
    </bean>
    
    <bean id="dataService" class="com.intsmaze.flink.base.service.DataService">
        <property name="jdbcTemplate" ref="jdbcTemplate"></property>
    </bean>

</beans>
```

#### config.properties

```
jdbc.user = intsmaze
jdbc.password = intsmaze
jdbc.url = jdbc:mysql://127.0.0.1:3306/flink-boot?useUnicode=true&characterEncoding=UTF-8
```

### 启动类示例

如下是SimpleClient（com.intsmaze.flink.client.SimpleClient）类的示例代码，该类继承了BaseFlink，可以看到对应实现的方法中分别设置如下：

*  public String getTopoName()：定义本作业的名称。
*  public String getConfigName()：定义本作业需要读取的spring配置文件的名称
*  public String getPropertiesName()：定义本作业需要读取的properties配置文件的名称。
*  public void createTopology(StreamExecutionEnvironment builder)：构造本作业的拓扑结构。

```
/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》  
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class SimpleClient extends BaseFlink {

    public static void main(String[] args) throws Exception {
        SimpleClient topo = new SimpleClient();
        topo.run(ParameterTool.fromArgs(args));
    }

    @Override
    public String getTopoName() {
        return "SimpleClient";
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

        DataStream<String> inputDataStrem = env.addSource(new SimpleDataSource());

        DataStream<String> processDataStream = inputDataStrem.flatMap(new SimpleFunction());

        processDataStream.print("输出结果");
    }

}
```

### 数据源

采用自定义数据源，用户需要编写自定义DataSource类，该类需要继承XXX抽象类，实现如下方法。

* public abstract void open(StormBeanFactory beanFactory):获取本作业在Spring配置文件中配置的bean对象。
* public abstract String sendMessage():本作业spout生成数据的方法，在该方法内编写业务逻辑产生源数据，产生的数据以String类型进行返回。

```
public class SimpleDataSource extends CommonDataSource {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	......

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ...//构造读取各类外部系统数据的连接实例
    }

    @Override
    public String sendMess() throws InterruptedException {
        Thread.sleep(1000);
		......
        MainData mainData = new MainData();
        ......//通过外部系统数据的连接实例读取外部系统数据，封装进MainData对象中，然后返回即可。
        return gson.toJson(mainData);
    }
}
```

### 业务逻辑实现

本作业计算的业务逻辑在Flink转换操作符中进行实现，一般来说开发者只需要实现flatMap算子即可以满足大部分算子的使用。

用户编写的自定义类需要继承com.intsmaze.flink.base.transform.CommonFunction抽象类，均需实现如下方法。

* public abstract String execute(String message)：本作业业务逻辑计算的方法，参数message为Kafka主题中读取过来的参数，默认参数为String类型，如果需要将处理的数据发送给Kakfa主题中，则要通过return将处理的数据返回即可。

```
public class SimpleFunction extends CommonFunction {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    
    @Override
    public String execute(String message) throws Exception {
        FlowData flowData = gson.fromJson(message, new TypeToken<FlowData>() {
        }.getType());

        String flowUUID = dataService.findUUID(flowData);
        if (StringUtils.isBlank(flowUUID)) {
            flowUUID = UUID.randomUUID().toString();
            flowData.setUuid(flowUUID);
            dataService.insertFlow(flowData);
        }
        return gson.toJson(flowData);
    }
}
```

#### CommonFunction

CommonFunction抽象类中默认在open方法中通过BeanFactory对象获取到了Spring容器中对于的dataService实例，对于Spring中的其他实例同理在SimpleFunction类中的open方法中获取即可。

```
public abstract class CommonFunction extends RichFlatMapFunction<String, String> {

    private IntCounter numLines = new IntCounter();

    protected DataService dataService;

    protected ApplicationContext beanFactory;

    @Override
    public void open(Configuration parameters) {
        getRuntimeContext().addAccumulator("num-FlatMap", this.numLines);

        ExecutionConfig.GlobalJobParameters globalJobParameters = getRuntimeContext()
                .getExecutionConfig().getGlobalJobParameters();
        beanFactory = BeanFactory.getBeanFactory((Configuration) globalJobParameters);

        dataService = beanFactory.getBean(DataService.class);
    }

    @Override
    public void flatMap(String value, Collector<String> out) throws Exception {
        this.numLines.add(1);
        String execute = execute(value);
        if (StringUtils.isNotBlank(execute)) {
            out.collect(execute);
        }
    }

    public abstract String execute(String message) throws Exception;

}
```

可以根据情况选择重写open(Configuration parameters)方法，同时重写的open(Configuration parameters)方法的第一行要调用父类的open(Configuration parameters)方法。

```
public void open(Configuration parameters){
	super.open(parameters);
	......
	//获取在Spring配置文件中配置的实例
	XXX xxx=beanFactory.getBean(XXX.class);
}
```

### 集群/本地运行
在自定义的Topology类编写Main方法，创建自定义的Topology对象后，调用对象的run(...)方法。
public class SimpleClient extends BaseFlink {

    /**
     * 本地启动参数  -isLocal local
     * 集群启动参数  -isIncremental isIncremental
     */
    public static void main(String[] args) throws Exception {
        SimpleClient topo = new SimpleClient();
        topo.run(ParameterTool.fromArgs(args));
    }
    
    .......     
   

## 演示地址

演示地址： [框架快速演示视频](https://www.ixigua.com/6922010036285735438?wid_try=1)

## 参与开发

首先谢谢大家支持，如果你希望参与开发，欢迎通过[Github](https://github.com/intsmaze/flink-boot "Github")上fork本项目，并Pull Request您的commit。

# 其他
## 联系我
下面是集成Spring生态的基础手册，加作者微信号获取更详细的开发手册，当然技术过硬自己摸索也只需3小时即可上手所有模块。
1. 生活不易，脑力代码不易，尊重劳动成果，可打赏博主 39.9元即可获得懒松鼠Flink-Boot相关核心配置文件（以及后续新特性集成代码）。
2. 也可以选择不打赏博主，公开版仅提供了Flink与以上Spring组件集成的所有代码，仅提供Flink与Spring基础集成的配置文件，其他组件的配置文件未提供，一般来说，自行研究框架4小时即可搞定。
3. 免费版本仅暴露XML的方式和spring集成的配置，会员版本提供了注解方式构造各种bean对象。
4. ps:我若衣食无忧必定无私奉献，39.9元的打赏只是为了让博主有继续花费精力迭代的动力帮助各位同行少加班。
微信号：intsmaze [微信二维码无法显示可跳转该页面扫码，微信转账即可](https://www.cnblogs.com/intsmaze/)


### 谁买了
目前一些在一些互联网公司的开发小哥都加我微信买了配置文件。

Flink业务场景解决方案可添加微信进行免费咨询（前提将业务场景以需求文档的方式编写好发送给我即可免费获得解决方案）。
![image](https://github.com/intsmaze/flink-boot/blob/master/wx.png)

**会员享受功能：**
1. 框架的详细手册和配置文件
2. 可以免费获取后续新增功能
3. 可以提想要集成的框架，我会根据是否有必要在一个月内集成
4. 框架使用上有问题，我会跟踪解决（PS：因为环境问题导致的不在售后范围）

详细文档目录结构如下：
![image](https://github.com/intsmaze/flink-boot/blob/master/mulu.png)

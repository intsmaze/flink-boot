package com.intsmaze.flink.groovy.test;

import com.intsmaze.flink.base.env.BeanFactory;
import com.intsmaze.flink.groovy.util.RedisKeys;
import org.nustaq.serialization.FSTConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.intsmaze.flink.groovy.bean.RuleConfigModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TestConfigGroovyRule {

    private FSTConfiguration fstConf = FSTConfiguration
            .createJsonConfiguration();

    private JedisPool jedisPool;

    public static void main(String[] args) throws Exception {
        BeanFactory beanFactory = new BeanFactory("test.xml");
        TestConfigGroovyRule groovy = beanFactory.getBean(TestConfigGroovyRule.class);

        //id必须唯一，不能重复
        RuleConfigModel newModel = new RuleConfigModel(
                2L,
                "intsmaze测试groovy脚本",
                "脚本红引入的类必须在这个工程的jvm中存在intsmaze",
                "import com.intsmaze.flink.groovy.GroovyInterface\n" +
                        "import com.intsmaze.flink.groovy.util.GroovyLogger\n\n" +
                        "GroovyInterface groovyDemo = GROOVY\n" +
                        "String age = AGE\n\n" +
                        "String sex = SEX\n\n" +
                        "if (age.contains(\"-\")||age.contains(\"_\")) {\n"+
                        "   return false\n" +
                        "}\n" +
                        "String regAge = groovyDemo.getAge(age);\n\n" +
                        "String regSex = groovyDemo.getSex(sex);\n\n"+
                        "LOG_STR = GroovyLogger.infor(\"age传入参数\", age,\"sex传入参数\", sex, \"处理后参数\",regSex, regAge)\n" +
//                        "LOG_STR = GroovyLogger.infor(\"age传入参数\", age, \"处理后参数\", regAge)\n" +
                        "return true",
                "39.9元配置文件带走");
        groovy.flushSingleRule(newModel);
    }

    /**
     * @author:YangLiu
     * @date:2018年5月21日 下午8:33:38
     * @describe:RuleConfigModel属性的id必须唯一，不能重复
     */
    public boolean flushSingleRule(RuleConfigModel newModel) throws Exception {
        Jedis jedis = jedisPool.getResource();
        Set<RuleConfigModel> list =  new HashSet<RuleConfigModel>();
        byte[] data = jedis.get(RedisKeys.getRuleConfig());
        if (data != null) {
            list = (HashSet<RuleConfigModel>) fstConf.asObject(data);
            if (list != null) {
                list.remove(newModel);
                list.add(newModel);
            }
        } else {
            list.add(newModel);
        }
        System.out.println(list);
        boolean result = false;
        //一般来说要加锁，但是不加锁也是可以容忍的
        result = "OK".equals(jedis
                .set(RedisKeys.getRuleConfig(), fstConf.asByteArray(list))) ? true : false;
        result = "OK".equals(jedis.set(RedisKeys.getRuleConfigVersion(),
                String.valueOf(System.currentTimeMillis()))) ? true
                : false;
        return result;
    }

    @SuppressWarnings({"unchecked"})
    public boolean removeSingleRule(long id) throws Exception {
        Jedis jedis = jedisPool.getResource();
        boolean flag = false;
        byte[] data = jedis.get(RedisKeys.getRuleConfig());
        if (data != null) {
            List<RuleConfigModel> list = (List<RuleConfigModel>) fstConf
                    .asObject(data);
            for (RuleConfigModel rule : list) {
                if (rule.getId() == id) {
                    list.remove(rule);
                    break;
                }
            }
            flag = "OK".equals(jedis.set(RedisKeys.getRuleConfig(),
                    fstConf.asByteArray(list))) ? true : false;
            jedis.set(RedisKeys.getRuleConfigVersion(),
                    String.valueOf(System.currentTimeMillis()));
        }
        return flag;
    }


    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

}

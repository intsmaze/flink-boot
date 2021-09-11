package com.intsmaze.flink.groovy.loadshell;

import com.intsmaze.flink.base.env.BeanFactory;
import com.intsmaze.flink.groovy.util.RedisKeys;
import org.nustaq.serialization.FSTConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.intsmaze.flink.groovy.bean.RuleConfigModel;
import java.util.ArrayList;
import java.util.List;


public class DaoGroovy {

    private FSTConfiguration fstConf = FSTConfiguration
            .createJsonConfiguration();

    private JedisPool jedisPool;

    public static void main(String[] args) throws Exception {
        BeanFactory beanFactory = new BeanFactory("redis-cud.xml");
        DaoGroovy groovy = beanFactory.getBean(DaoGroovy.class);

        //id必须唯一，不能重复
        RuleConfigModel newModel = new RuleConfigModel(
                2L,
                "intsmaze测试groovy脚本",
                "脚本红引入的类必须在这个工程的jvm中存在intsmaze",
                "import org.intsmaze.GroovyDemo\nimport org.intsmaze.GroovyLogger\n\nGroovyDemo groovyDemo = GROOVY\nString age = AGE\n\nif (age.contains(\"-\")||age.contains(\"_\")) {\n    return false\n}\nString regAge = groovyDemo.getSex(age);\n\nLOGTAG = GroovyLogger.text(\"传入参数\", age, \"处理后参数\", regAge)\nreturn true",
                "39.9元配置文件带走", 1);
        groovy.flushSingleRule(newModel);


    }

    /**
     * @author:YangLiu
     * @date:2018年5月21日 下午8:33:38
     * @describe:RuleConfigModel属性的id必须唯一，不能重复
     */
    public boolean flushSingleRule(RuleConfigModel newModel) throws Exception {
        Jedis jedis = jedisPool.getResource();
        List<RuleConfigModel> list = null;
        byte[] data = jedis.get(RedisKeys.getRuleConfig());
        if (data != null) {
            list = (List<RuleConfigModel>) fstConf.asObject(data);
            if (list != null) {
                list.add(newModel);
            }
        } else {
            list = new ArrayList<RuleConfigModel>();
            list.add(newModel);
        }
        boolean result = false;
        result = "OK".equals(jedis.set(RedisKeys.getRuleConfigVersion(),
                String.valueOf(System.currentTimeMillis()))) ? true
                : false;
        result = "OK".equals(jedis
                .set(RedisKeys.getRuleConfig(), fstConf.asByteArray(list))) ? true : false;
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
        }
        jedis.set(RedisKeys.getRuleConfigVersion(),
                String.valueOf(System.currentTimeMillis()));
        return flag;
    }


    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

}

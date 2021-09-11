package com.intsmaze.flink.groovy;

import com.google.common.base.Preconditions;
import groovy.lang.Script;
import com.intsmaze.flink.groovy.util.RedisKeys;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.nustaq.serialization.FSTConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.intsmaze.flink.groovy.bean.ScriptCacheMapping;
import com.intsmaze.flink.groovy.bean.RuleConfigModel;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;


public class CacheGroovyService {

    private final Logger logger = LoggerFactory.getLogger(CacheGroovyService.class);

    private static FSTConfiguration jsonConfiguration = FSTConfiguration.createJsonConfiguration();

    private JedisPool jedisPool;

    //这个是最主要的，脚本调用
    private static ScriptCacheMapping scriptCacheMapping = new ScriptCacheMapping();

    public static Map ScriptTextmap = new HashMap();

    private static volatile boolean inited = false;

    private static volatile Thread updateThread;

    private String ruleConfigVersion;

    public void init() {
        synchronized (CacheGroovyService.class) {
            if (inited) {
                return;
            }
            initRuleConfig();
            inited = true;
            updateThread();
        }
    }

    private void updateThread() {
        if (updateThread != null) {
            return;
        }
        String name = "groovy-CacheService-Update" + new Random().nextInt(100);
        updateThread = new UpdateThread(name);
        logger.info("Begin updateThread...");
        updateThread.start();
    }

    private class UpdateThread extends Thread {
        public UpdateThread(String threadName) {
            super.setName(threadName);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(180);
                } catch (InterruptedException e1) {
                    logger.error("", e1);
                }
                logger.info("Begin update groovy data cache...");
                try {
                    initRuleConfig();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
    }


    public void initRuleConfig() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String newVersion = jedis.get(RedisKeys.getRuleConfigVersion());
            // 判断Redis中的缓存版本，如果有新版本则更新数据
            if (!StringUtils.equals(newVersion, ruleConfigVersion)) {
                byte[] data = jedis.get(RedisKeys.getRuleConfig());
                Preconditions.checkNotNull(data);
                // 规则更新
                List<RuleConfigModel> rules = (List<RuleConfigModel>) asObject(data);
                for (RuleConfigModel rule : rules) {
                    Long id = rule.getId();
                    cacheScript(id, rule.getRuleScript(), rule.getHashcode());
                }
            }
            ruleConfigVersion = newVersion;
        } finally {
            jedisPool.close();
        }
    }

    /**
     * 缓存脚本
     *
     * @param id
     * @param scriptContent
     * @param hashcode
     */
    private void cacheScript(Long id, String scriptContent, String hashcode) {
        // 判断新的脚本内容与缓存是否有差异，如果有则重新编译
        if (!scriptCacheMapping.isDifference(id, hashcode)) {
            scriptCacheMapping.addScript(id, hashcode, GroovyScriptExecutor.getDefaultShell().parse(scriptContent));
            ScriptTextmap.put(id, scriptContent);
            logger.info("Finding a new script, recompile and cache, rule id: {}.", id);
        }
    }

    public static ConcurrentMap<Long, Pair<String, Script>> getScriptCache() {
        return scriptCacheMapping.getScriptCache();
    }

    public synchronized Object asObject(byte[] b) {
        try {
            return jsonConfiguration.asObject(b);
        } catch (Exception e) {
            try {
                throw new RuntimeException(new String(b, "UTF-8"), e);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }


}
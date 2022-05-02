package com.intsmaze.flink.groovy.util;

import redis.clients.util.SafeEncoder;

public class RedisKeys {

    public static byte[] getRuleConfig() {
        return SafeEncoder.encode("rule_config_test_2");
    }

    /**
     * grooby脚本版本
     */
    public static String getRuleConfigVersion() {
        return "RuleConfigVersion";
    }

}

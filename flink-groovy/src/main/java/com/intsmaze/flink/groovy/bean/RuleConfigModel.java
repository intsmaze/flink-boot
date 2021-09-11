package com.intsmaze.flink.groovy.bean;

import java.io.Serializable;

public class RuleConfigModel implements Serializable {

    private Long id;
    private String ruleName;
    private String ruleDesc;
    private String ruleScript;
    private String hashcode;
    private Integer ruleType;

    public RuleConfigModel(Long id, String ruleName, String ruleDesc,
                           String ruleScript, String hashcode, Integer ruleType) {
        super();
        this.id = id;
        this.ruleName = ruleName;
        this.ruleDesc = ruleDesc;
        this.ruleScript = ruleScript;
        this.hashcode = hashcode;
        this.ruleType = ruleType;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public String getRuleScript() {
        return ruleScript;
    }

    public void setRuleScript(String ruleScript) {
        this.ruleScript = ruleScript;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public RuleConfigModel() {
        super();
    }

}
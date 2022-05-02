package com.intsmaze.flink.groovy.bean;

import java.io.Serializable;
import java.util.Objects;

public class RuleConfigModel implements Serializable {

    private Long id;
    private String ruleName;
    private String ruleDesc;
    private String ruleScript;

    private String hashcode;
    private Integer ruleType;

    public RuleConfigModel(Long id, String ruleName, String ruleDesc,
                           String ruleScript, String hashcode) {
        super();
        this.id = id;
        this.ruleName = ruleName;
        this.ruleDesc = ruleDesc;
        this.ruleScript = ruleScript;
        this.hashcode = hashcode;
//        this.ruleType = ruleType;
    }

//    public Integer getRuleType() {
//        return ruleType;
//    }
//
//    public void setRuleType(Integer ruleType) {
//        this.ruleType = ruleType;
//    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleConfigModel that = (RuleConfigModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ruleName, that.ruleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruleName);
    }

    @Override
    public String toString() {
        return "RuleConfigModel{" +
                "id=" + id +
                ", ruleName='" + ruleName + '\'' +
                ", ruleDesc='" + ruleDesc + '\'' +
                ", ruleScript='" + ruleScript + '\'' +
                ", hashcode='" + hashcode + '\'' +
                '}';
    }
}
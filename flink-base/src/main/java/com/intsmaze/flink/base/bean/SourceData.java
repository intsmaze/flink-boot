package com.intsmaze.flink.base.bean;

import java.sql.Timestamp;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class SourceData {

    /**
     * 是否触发执行的标志
     */
    private String flag;

    private String uuid;

    /**
     * sqlClient也用的这个字段，RowNumberBean 中进行比较，字段类型也是它
     */
    private Timestamp testTime;

    private int seqId;

    private int stepNumber;

    private String stepType;

    private int cycleNumber;

    private int batchNumber;

    private String flowId;

    private String threadName;

    /**
     * 用于eventtime
     */
    private Timestamp receiveTime;

    public SourceData()
    {}

    public SourceData(String flag, String uuid, Timestamp testTime, int seqId, int stepNumber, String stepType, int cycleNumber,
                      int batchNumber, String flowId, String threadName, Timestamp receiveTime) {
        this.flag = flag;
        this.uuid = uuid;
        this.testTime = testTime;
        this.seqId = seqId;
        this.stepNumber = stepNumber;
        this.stepType = stepType;
        this.cycleNumber = cycleNumber;
        this.batchNumber = batchNumber;
        this.flowId = flowId;
        this.threadName = threadName;
        this.receiveTime = receiveTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getTestTime() {
        return testTime;
    }

    public void setTestTime(Timestamp testTime) {
        this.testTime = testTime;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        batchNumber = batchNumber;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Timestamp getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Timestamp receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public String toString() {
        return "SourceData{" +
                "flowId='" + flowId + '\'' +
                ", cycleNumber=" + cycleNumber +
                ", flag='" + flag + '\'' +
                ", seqId=" + seqId +
                ", stepNumber='" + stepNumber + '\'' +
                ", stepType='" + stepType + '\'' +
                '}';
    }
}

package com.intsmaze.flink.client.bean;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class SwitchState {
    private Boolean isNewCycle = false;

    public Boolean getNewCycle() {
        return isNewCycle;
    }

    public void setNewCycle(Boolean newCycle) {
        isNewCycle = newCycle;
    }
}

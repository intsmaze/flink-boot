package com.intsmaze.flink.annotation;

import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
@Component
public class DataServiceAnnotation {

    @Autowired
    private DataService dataService;

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public void insertFlow(FlowData flowData) throws Exception {
        dataService.insertFlow(flowData);
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public String findUUID(FlowData flowData) {
        return dataService.findUUID(flowData);
    }

}

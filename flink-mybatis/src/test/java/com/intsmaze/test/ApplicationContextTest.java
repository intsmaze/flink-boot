package com.intsmaze.test;

import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.mybatis.service.FlowService;
import com.intsmaze.flink.mybatis.service.impl.FlowServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;


public class ApplicationContextTest {


    public static void main(String[] args) {
        ApplicationContext beanConf = new ClassPathXmlApplicationContext("spring-container.xml");
        FlowService flowService = beanConf.getBean(FlowServiceImpl.class);
        List<FlowData> users = flowService.findAll();
        System.out.println(users);
    }

}
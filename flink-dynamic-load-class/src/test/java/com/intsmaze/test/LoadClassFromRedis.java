package com.intsmaze.test;

import com.intsmaze.flink.dynamic.base.ClassLoadService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoadClassFromRedis {

    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        ApplicationContext ct = new ClassPathXmlApplicationContext("spring-classload.xml");
        ClassLoadService classLoadService = (ClassLoadService) ct.getBean("classLoadService");
        classLoadService.init();
    }

}

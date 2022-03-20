package com.intsmaze.test;

import com.intsmaze.flink.dynamic.Constant;
import com.intsmaze.flink.dynamic.DynamicService;
import com.intsmaze.flink.dynamic.base.FileSystemClassLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class LoadClassFromRedis {

    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        ApplicationContext ct = new ClassPathXmlApplicationContext("spring-classload.xml");
        FileSystemClassLoader fileSystemClassLoader = (FileSystemClassLoader) ct.getBean("fileSystemClassLoader");
        Class<?> c = fileSystemClassLoader.loadClass(Constant.PACKAGE_NAME  +Constant.CLASS_NAME);
        DynamicService o = (DynamicService) c.newInstance();
        o.executeService("flink boot,intsmaze");
    }

}

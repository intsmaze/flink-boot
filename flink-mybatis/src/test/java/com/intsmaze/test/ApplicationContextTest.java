package com.intsmaze.test;

import com.intsmaze.flink.mybatis.dto.UserDto;
import com.intsmaze.flink.mybatis.service.UserService;
import com.intsmaze.flink.mybatis.service.impl.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;


public class ApplicationContextTest {


    public static void main(String[] args) {
        ApplicationContext beanConf = new ClassPathXmlApplicationContext("spring-container.xml");
        UserService userService = beanConf.getBean(UserServiceImpl.class);
        List<UserDto> users = userService.FindAllUser();
        System.out.println(users);
    }

}
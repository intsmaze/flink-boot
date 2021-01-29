package com.intsmaze.dubbo.provider;

/**
 * @author ：intsmaze
 * @date ：Created in 2021/1/26 22:06
 * @description： https://www.cnblogs.com/intsmaze/
 * @modified By：
 */
public class DemoServiceImpl implements DubboService {
    public String sayHello(String name) {
        System.out.println(Thread.currentThread().getName()+"--------------------");
        return "dubbo provider return mess: " + name;
    }
}

package com.intsmaze.dubbo.provider;

/**
 * @author ：intsmaze
 * @date ：Created in 2021/1/26 22:06
 * @description： https://www.cnblogs.com/intsmaze/
 * @modified By：
 */
public class DemoServiceImpl implements DubboService {
    public String flinkDealMess(String value) {
        System.out.println(Thread.currentThread().getName()+"--------------------");
        return "dubbo provider return mess: " + value;
    }
}

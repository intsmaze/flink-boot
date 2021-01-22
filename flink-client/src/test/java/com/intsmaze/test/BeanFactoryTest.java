package com.intsmaze.test;

import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.service.DataService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author ：intsmaze
 * @date ：Created in 2021/1/22 11:22
 * @description： https://www.cnblogs.com/intsmaze/
 * @modified By：
 */
public class BeanFactoryTest {

    public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("topology-base.xml");
		DataService da = applicationContext.getBean(DataService.class);
		FlowData flowData=new FlowData();
		flowData.setBillNumber("1231");
		System.out.println(da.findUUID(flowData));

    }
}

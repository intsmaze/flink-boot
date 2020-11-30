package com.intsmaze.test;

import com.intsmaze.flink.validate.ValidatorUtil;
import com.intsmaze.flink.validate.bean.FlowValidateData;

import java.util.Map;

/**
 * @author ：intsmaze
 * @date ：Created in 2020/11/22 13:09
 * @description： https://www.cnblogs.com/intsmaze/
 * @modified By：
 */
public class ValidatorTest {


    public static void main(String[] args) {

        FlowValidateData s = new FlowValidateData();
        long startTime = System.currentTimeMillis();
        System.out.println(ValidatorUtil.validate(s));
        print(ValidatorUtil.validate(s));
        System.out.println("===============耗时(毫秒)=" + (System.currentTimeMillis() - startTime));

        s.setSubTestItem("chenwb");
        s.setBarcode("10");
        s.setBillNumber("2016-09-01");
        s.setFlowStatus("intsmaze");
        startTime = System.currentTimeMillis();
        print(ValidatorUtil.validate(s));
        System.out.println("===============耗时(毫秒)=" + (System.currentTimeMillis() - startTime));
    }

    private static void print(Map<String, StringBuffer> errorMap) {
        if (errorMap != null) {
            for (Map.Entry<String, StringBuffer> m : errorMap.entrySet()) {
                System.out.println(m.getKey() + ":" + m.getValue().toString());
            }
        }
    }
}

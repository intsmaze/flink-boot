package com.intsmaze.flink.client.task.source;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intsmaze.flink.base.bean.MainData;
import com.intsmaze.flink.base.transform.CommonDataSource;
import org.apache.flink.configuration.Configuration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class SimpleDataSource extends CommonDataSource {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    int seqId = 1;

    int num = 1;

    List<String> stepTypeList;

    List<String> subTestItemList;

    String billNumberPre = "intsmaze-";

    String barcodePre = "1992-";

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        stepTypeList = new ArrayList<String>();
        stepTypeList.add("充电");
        stepTypeList.add("放电");
        stepTypeList.add("搁置");
        stepTypeList.add("放电");
        stepTypeList.add("搁置");

        subTestItemList = new ArrayList<String>();
        subTestItemList.add("测试方法-1");
        subTestItemList.add("测试方法-22");
        subTestItemList.add("测试方法-333");
        subTestItemList.add("测试方法-4444");
        subTestItemList.add("测试方法-55555");
        subTestItemList.add("测试方法-666666");
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public String sendMess() throws InterruptedException {
        Thread.sleep(1000);
        seqId++;
        num++;
        if (num == 100) {
            num = 1;
        }
        Random ra = new Random();
        int index = ra.nextInt(subTestItemList.size());
        String subTestItem = subTestItemList.get(index);


        int stepIndex = seqId % stepTypeList.size();
        String stepType = stepTypeList.get(stepIndex);
        MainData mainData = new MainData();
        mainData.setBillNumber(billNumberPre + ((num + 1) * 11111));
        mainData.setBarcode(barcodePre + ((num + 1) * 11111));
        mainData.setSubTestItem(subTestItem);
        mainData.setFlowName("intsmaze");
        mainData.setStepType(stepType);
        mainData.setSeqId(seqId);
        mainData.setStepNumber(seqId % 28);
        mainData.setTestTime(new Timestamp(System.currentTimeMillis()));
        return gson.toJson(mainData);
    }
}

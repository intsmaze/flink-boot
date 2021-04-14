package com.intsmaze.flink.validate.tsak;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intsmaze.flink.base.transform.CommonFunction;
import com.intsmaze.flink.validate.bean.FlowValidateData;
import com.intsmaze.flink.validate.ValidatorUtil;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.configuration.Configuration;

import java.util.Map;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class ValidateFlatMap extends CommonFunction {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 记录接收的数据的数量
     */
    private IntCounter numLines = new IntCounter();

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        getRuntimeContext().addAccumulator("num-GsonFlatMap", this.numLines);
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public String execute(String value) {
        FlowValidateData flowData = gson.fromJson(value, new TypeToken<FlowValidateData>() {
        }.getType());

        this.numLines.add(1);
        Map<String, StringBuffer> validate = ValidatorUtil.validate(flowData);
        if (validate != null) {
            System.out.println(validate);
            return null;
        }
        return gson.toJson(flowData);
    }
}

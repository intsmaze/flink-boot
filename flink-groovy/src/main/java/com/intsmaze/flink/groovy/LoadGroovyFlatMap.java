package com.intsmaze.flink.groovy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.transform.BuiltinRichFlatMapFunction;
import com.intsmaze.flink.groovy.service.CacheGroovyService;
import com.intsmaze.flink.groovy.service.GroovyRuleCalc;
import com.intsmaze.flink.groovy.service.GroovyScriptExecutor;
import groovy.lang.Binding;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoadGroovyFlatMap extends BuiltinRichFlatMapFunction {

    private Logger logger = LoggerFactory.getLogger(LoadGroovyFlatMap.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    GroovyScriptExecutor scriptExecutor;


    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2022/07/10 18:33
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        CacheGroovyService cacheService = beanFactory.getBean(CacheGroovyService.class);
        cacheService.init();

        scriptExecutor = beanFactory.getBean(GroovyScriptExecutor.class);

    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2022/07/10 18:33
     */
    @Override
    public String execute(String message) {
        FlowData flowData = gson.fromJson(message, new TypeToken<FlowData>() {
        }.getType());

        Binding shellContext = new Binding();
        shellContext.setVariable("AGE", flowData.getFlowName());
        shellContext.setVariable("SEX", flowData.getBarcode());
        shellContext.setVariable("GROOVY", new GroovyInterface());

        //*********************配置脚本核心计算处*******************
        if(GroovyRuleCalc.executeRuleCalcAll(scriptExecutor, shellContext))
        {

        }
        return null;
    }
}

package com.intsmaze.flink.retry;

import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;


/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
@Component
@EnableRetry
public class DataServiceWithRetry  {

    @Autowired
    private DataService dataService;

    public static Logger LOG = LoggerFactory.getLogger(DataServiceWithRetry.class);

    int makeError = 1;

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     * 重试3次还失败抛出异常，delay第一次多少秒间隔重试，multiplier递增倍数（即下次间隔是上次的多少倍）
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 1.5))
    public void insertFlow(FlowData flowData) throws Exception {
        LOG.info("insertFlow method：{}",flowData.toString());
        makeError++;
        if (makeError % 5 == 1) {
            LOG.info("throw new Exception：{}",flowData.toString());
            throw new Exception("手动抛出异常..");
        }
        dataService.insertFlow(flowData);
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     * 重试3次还失败抛出异常，delay第一次多少秒间隔重试，multiplier递增倍数（即下次间隔是上次的多少倍）
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 1.5))
    @Cacheable(value="FlowData.findUUID",key="#flowData.subTestItem+'-'+#flowData.billNumber+'-'+#flowData.barcode")
    public String findUUID(FlowData flowData) {
        LOG.info("执行findUUID方法");
        return dataService.findUUID(flowData);
    }

}

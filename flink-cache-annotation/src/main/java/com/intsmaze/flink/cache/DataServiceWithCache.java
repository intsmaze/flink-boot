package com.intsmaze.flink.cache;

import com.intsmaze.flink.base.bean.FlowData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
@Component
public class DataServiceWithCache {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static Logger LOG = LoggerFactory.getLogger(DataServiceWithCache.class);

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public void insertFlow(FlowData flowData) throws Exception {
        LOG.info("执行insertFlow方法");
        String sql = "insert into flow(uuid, subTestItem,billNumber,barcode,flowName,flowStatus) values (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, flowData.getUuid(), flowData.getSubTestItem(), flowData.getBillNumber(), flowData.getBarcode(), flowData.getFlowName(), flowData.getFlowStatus());

    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
//    @Cacheable(value = "FlowData.findUUID", key = "#flowData.subTestItem+'-'+#flowData.billNumber+'-'+#flowData.barcode")
    @Cacheable(value = "FlowData.findUUID", key = "#flowData.subTestItem")
    public String findUUID(FlowData flowData) {
        LOG.info("执行findUUID方法");
        System.out.println("执行findUUID方法----------");
        String sql = "select uuid from flow where subTestItem = ? and billNumber=? and barcode=?";
        List<String> flowList = jdbcTemplate.queryForList(sql, String.class, flowData.getSubTestItem(), flowData.getBillNumber(), flowData.getBarcode());
        if (!flowList.isEmpty()) {
            return flowList.get(0);
        }
        return null;

    }

}

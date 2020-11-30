package com.intsmaze.flink.base.service;

import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.bean.ResultBean;
import com.intsmaze.flink.base.bean.SourceData;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class DataService {

    public static WeakHashMap<String, String> weakHashMap = new WeakHashMap();

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public void insertFlow(FlowData flowData) throws Exception {
        String sql = "insert into flow(uuid, subTestItem,billNumber,barcode,flowName,flowStatus) values (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, flowData.getUuid(), flowData.getSubTestItem(), flowData.getBillNumber(), flowData.getBarcode(), flowData.getFlowName(), flowData.getFlowStatus());
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public String findUUID(FlowData flowData) {
        String key = flowData.getSubTestItem() + flowData.getBillNumber() + flowData.getBarcode();
        String value = weakHashMap.get(key);
        if (value == null) {
            String sql = "select uuid from flow where subTestItem = ? and billNumber=? and barcode=?";
            List<String> flowList = jdbcTemplate.queryForList(sql, String.class, flowData.getSubTestItem(), flowData.getBillNumber(), flowData.getBarcode());
            if (!flowList.isEmpty()) {
                weakHashMap.put(key, flowList.get(0));
                return flowList.get(0);
            }
        }
        return value;
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public List<Map<String, Object>> findVariable() {
        String sql = "select name,value from variable ";
        List<Map<String, Object>> variableList = jdbcTemplate.queryForList(sql);
        return variableList;
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public void insertSource(SourceData sourceData) {
        String sql = "insert into sourcedata (uuid,testTime,seq_id,stepNumber,stepType,cycleNumber,flowId,threadName)" +
                " values (?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, sourceData.getUuid(), sourceData.getTestTime(), sourceData.getSeqId(),
                sourceData.getStepNumber(), sourceData.getStepType(), sourceData.getCycleNumber(),
                sourceData.getFlowId(), sourceData.getThreadName());
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public void batchInsertSource(List<SourceData> sourceDataList) {
        String sql = "insert into sourcedata (uuid,testTime,seq_id,stepNumber,stepType,cycleNumber,flowId,threadName,receiveTime)" +
                " values (?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, new
                BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        SourceData sourceData = sourceDataList.get(i);
                        ps.setString(1, sourceData.getUuid());
                        ps.setTimestamp(2, new Timestamp(sourceData.getTestTime().getTime()));
                        ps.setInt(3, sourceData.getSeqId());
                        ps.setInt(4, sourceData.getStepNumber());
                        ps.setString(5, sourceData.getStepType());
                        ps.setInt(6, sourceData.getCycleNumber());
                        ps.setString(7, sourceData.getFlowId());
                        ps.setString(8, sourceData.getThreadName());
                        ps.setTimestamp(9,  new Timestamp(sourceData.getReceiveTime().getTime()));
                    }

                    @Override
                    public int getBatchSize() {
                        return sourceDataList.size();
                    }
                });
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public void batchInsertResultBean(List<ResultBean> dataList) {
        String sql = "insert into ResultBean (flowId,cycleNumber,total,stepNumber,stepType)" +
                " values (?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, new

                BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ResultBean resultBean = dataList.get(i);
                        ps.setString(1, resultBean.getFlowId());
                        ps.setInt(2, resultBean.getCycleNumber());
                        ps.setLong(3, resultBean.getTotal());
                        ps.setInt(4, resultBean.getStepNumber());
                        ps.setString(5, resultBean.getStepType());
                    }

                    @Override
                    public int getBatchSize() {
                        return dataList.size();
                    }
                });
    }
}

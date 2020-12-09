package com.intsmaze.flink.mybatis.service.impl;

import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.mybatis.mapper.FlowMapper;
import com.intsmaze.flink.mybatis.service.FlowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowServiceImpl implements FlowService {

    @Resource
    private FlowMapper flowMapper;

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public List<FlowData> findAll(){
        return flowMapper.findAll();
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
    public String findUUID(FlowData flowData) {
        return flowMapper.findUUID(flowData);
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
    public void insertFlow(FlowData flowData) {
        flowMapper.insertFlow(flowData);
    }
}
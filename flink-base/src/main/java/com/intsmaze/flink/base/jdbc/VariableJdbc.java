//package com.intsmaze.flink.base.jdbc;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.util.List;
//import java.util.Map;
//
//
///**
// * github地址: https://github.com/intsmaze
// * 博客地址：https://www.cnblogs.com/intsmaze/
// * 出版书籍《深入理解Flink核心设计与实践原理》
// *
// * @auther: intsmaze(刘洋)
// * @date: 2020/10/15 18:33
// */
//public class VariableJdbc {
//
//    private JdbcTemplate jdbcTemplate;
//
//    public JdbcTemplate getJdbcTemplate() {
//        return jdbcTemplate;
//    }
//
//    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    /**
//     * github地址: https://github.com/intsmaze
//     * 博客地址：https://www.cnblogs.com/intsmaze/
//     * 出版书籍《深入理解Flink核心设计与实践原理》
//     *
//     * @auther: intsmaze(刘洋)
//     * @date: 2020/10/15 18:33
//     */
//    public List<Map<String, Object>> findVariable() {
//        String sql = "select name,value from variable ";
//        List<Map<String, Object>> variableList = jdbcTemplate.queryForList(sql);
//        return variableList;
//    }
//
//}

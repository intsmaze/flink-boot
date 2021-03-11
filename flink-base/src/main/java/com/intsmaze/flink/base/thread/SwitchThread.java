//package com.intsmaze.flink.base.thread;
//
//import com.google.common.collect.Maps;
//import com.intsmaze.flink.base.jdbc.VariableJdbc;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.TimeUnit;
//
///**
// * github地址: https://github.com/intsmaze
// * 博客地址：https://www.cnblogs.com/intsmaze/
// * 出版书籍《深入理解Flink核心设计与实践原理》
// *
// * @auther: intsmaze(刘洋)
// * @date: 2020/10/15 18:33
// */
//public class SwitchThread {
//
//    public static String isSleep = "isSleep";
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    public static ConcurrentMap<String, Object> activityConfigs = Maps.newConcurrentMap();
//
//    private static volatile boolean inited = false;
//
//    private VariableJdbc variableJdbc;
//
//    private static volatile Thread updateThread;
//
//    /**
//     * github地址: https://github.com/intsmaze
//     * 博客地址：https://www.cnblogs.com/intsmaze/
//     * 出版书籍《深入理解Flink核心设计与实践原理》
//     *
//     * @auther: intsmaze(刘洋)
//     * @date: 2020/10/15 18:33
//     */
//    public void init() {
//        synchronized (SwitchThread.class) {
//            if (inited) {
//                return;
//            }
//            logger.info("初始化配置数据......");
//            initVariableDictTry();
//            inited = true;
//            start();
//        }
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
//    private void initVariableDictTry() {
//        List<Map<String, Object>> variableList = variableJdbc.findVariable();
//        for (int i = 0; i < variableList.size(); i++) {
//            Map<String, Object> stringObjectMap = variableList.get(i);
//            String name = (String) stringObjectMap.get("name");
//            String value = (String) stringObjectMap.get("value");
//            activityConfigs.put(name, value);
//        }
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
//    public void start() {
//        synchronized (SwitchThread.class) {
//            if (updateThread != null) {
//                return;
//            }
//            updateThread = new UpdateThread();
//            updateThread.start();
//        }
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
//    private class UpdateThread extends Thread {
//        /**
//         * github地址: https://github.com/intsmaze
//         * 博客地址：https://www.cnblogs.com/intsmaze/
//         * 出版书籍《深入理解Flink核心设计与实践原理》
//         *
//         * @auther: intsmaze(刘洋)
//         * @date: 2020/10/15 18:33
//         */
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    TimeUnit.MINUTES.sleep(1);
//                } catch (InterruptedException e1) {
//                }
//                logger.info("更新配置数据......");
//                try {
//                    initVariableDictTry();
//                } catch (Exception e) {
//                    logger.error("", e);
//                }
//            }
//        }
//    }
//
//    public VariableJdbc getVariableJdbc() {
//        return variableJdbc;
//    }
//
//    public void setVariableJdbc(VariableJdbc variableJdbc) {
//        this.variableJdbc = variableJdbc;
//    }
//}

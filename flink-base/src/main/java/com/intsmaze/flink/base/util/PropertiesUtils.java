package com.intsmaze.flink.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class PropertiesUtils {

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public static Properties getProperties(String path) throws IOException {
        InputStream ips = PropertiesUtils.class
                .getClassLoader()
                .getResourceAsStream(path);
        Properties props = new Properties();
        props.load(ips);
        ips.close();
        return props;
    }


}

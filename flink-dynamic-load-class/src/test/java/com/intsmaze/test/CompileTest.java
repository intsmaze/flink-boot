package com.intsmaze.test;


import com.intsmaze.flink.dynamic.Constant;
import com.intsmaze.flink.dynamic.DynamicService;
import com.intsmaze.flink.dynamic.base.utils.CompileJavaFileRedisTemplate;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author:YangLiu
 * @date:2018年5月18日 下午5:13:44
 * @describe:这个加载class文件，发现经常java.lang.ClassNotFoundException， 用自己自定义的FileSystemClassLoader没有问题。
 */
public class CompileTest {

    public static void main(String[] args) throws Exception {

        URL[] urls = new URL[]{new URL("file:/"
                + CompileJavaFileRedisTemplate.CLASS_PATH + "/" + Constant.CLASS_NAME + ".class")};
        ClassLoader classLoader = new URLClassLoader(urls);
        Class<?> c = classLoader.loadClass(Constant.CLASS_NAME);
        DynamicService o = (DynamicService) c.newInstance();
        o.executeService("ClassNotFoundException");
    }
}


package com.intsmaze.flink.dynamic.base;


import com.intsmaze.flink.dynamic.DynamicService;
import com.intsmaze.flink.dynamic.base.utils.CompileJavaFileToRedisTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class ClassLoadService {

    private FileSystemClassLoader fileSystemClassLoader;

//    public void init() {
//        try {
//            fileSystemClassLoader.setRootDir(CompileJavaFileToRedisTemplate.CLASS_PATH);
//            String[] arr = this.getClassNames();
//            List<String> list = Arrays.asList(arr);
//            Iterator<String> it = list.iterator();
//            while (it.hasNext()) {
//                String className = it.next();
//                Class<?> clazz = fileSystemClassLoader.loadClass(className);
//                DynamicService obj = (DynamicService) clazz.newInstance();
//                obj.executeService("123");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public DynamicService init() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            fileSystemClassLoader.setRootDir(CompileJavaFileToRedisTemplate.CLASS_PATH);
            String[] arr = this.getClassNames();
            List<String> list = Arrays.asList(arr);
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                String className = it.next();
                Class<?> clazz = fileSystemClassLoader.loadClass(className);
                DynamicService obj = (DynamicService) clazz.newInstance();
                return obj;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @return
     * @throws
     * @author intsmaze
     * @description: https://www.cnblogs.com/intsmaze/
     * 模拟从数据库中读取所有要加载的类的全路径
     * @date : 2020/6/6 18:37
     * @Param
     */
    public String[] getClassNames() {
        String[] classNameArr = {
                "com.intsmaze.classload.service.impl.ClassLoaderOdps"};
        return classNameArr;
    }

    public FileSystemClassLoader getFileSystemClassLoader() {
        return fileSystemClassLoader;
    }

    public void setFileSystemClassLoader(FileSystemClassLoader fileSystemClassLoader) {
        this.fileSystemClassLoader = fileSystemClassLoader;
    }


}

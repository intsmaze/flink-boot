package com.intsmaze.flink.dynamic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intsmaze.flink.base.bean.FlowData;
import com.intsmaze.flink.base.transform.BuiltinRichFlatMapFunction;
import com.intsmaze.flink.dynamic.base.FileSystemClassLoader;
import com.intsmaze.flink.dynamic.base.utils.CompileJavaFileRedisTemplate;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2021/07/10 18:33
 */
public class LoadClassFlatMap extends BuiltinRichFlatMapFunction {

    private Logger logger = LoggerFactory.getLogger(LoadClassFlatMap.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private HashMap<String, DynamicService> dynamicServiceHashMap = new HashMap<>();

    private FileSystemClassLoader fileSystemClassLoader;


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
        String[] classNameArr = {Constant.PACKAGE_NAME + Constant.CLASS_NAME};
        return classNameArr;
    }

    public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            fileSystemClassLoader.setRootDir(CompileJavaFileRedisTemplate.CLASS_PATH);
            String[] arr = this.getClassNames();
            List<String> list = Arrays.asList(arr);
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                String className = it.next();
                Class<?> clazz = fileSystemClassLoader.loadClass(className);
                DynamicService obj = (DynamicService) clazz.newInstance();
                dynamicServiceHashMap.put(className, obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2021/07/10 18:33
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        fileSystemClassLoader = (FileSystemClassLoader) beanFactory.getBean("fileSystemClassLoader");
        init();
//        Class<?> c = fileSystemClassLoader.loadClass(getClassNames()[0]);
//        DynamicService o = (DynamicService) c.newInstance();
//        o.executeService("flink boot,intsmaze");
//        ClassLoadService classLoadService = (ClassLoadService) beanFactory.getBean("classLoadService");
//        dynamicService = classLoadService.init();
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2021/07/10 18:33
     */
    @Override
    public String execute(String message) {
        FlowData flowData = gson.fromJson(message, new TypeToken<FlowData>() {
        }.getType());
        Collection<DynamicService> values = dynamicServiceHashMap.values();
        for (Iterator<DynamicService> iterator = values.iterator(); iterator.hasNext(); ) {
            DynamicService next = iterator.next();
            next.executeService(gson.toJson(flowData));
        }
        return null;
    }
}

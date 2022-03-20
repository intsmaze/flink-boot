package com.intsmaze.flink.dynamic.base.utils;

import com.intsmaze.flink.dynamic.DynamicService;
import com.intsmaze.flink.dynamic.LoadClassFlatMap;
import com.intsmaze.flink.dynamic.base.FileSystemClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;


import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author intsmaze
 * @description: https://www.cnblogs.com/intsmaze/
 * 该类负责将编写的java类编译为class文件后加载进redis中
 * @date : 2020/6/6 18:49
 */
public class CompileJavaFileRedisTemplate {

    private Logger logger = LoggerFactory.getLogger(CompileJavaFileRedisTemplate.class);

    private JedisPool jedisPool;

    private FileSystemClassLoader fileSystemClassLoader;

    public static final String CLASS_PATH = "c:/home/intsmaze/classload";

    public static final String KEY_NAME = "classload:";

    private Map<String, DynamicService> dynamicServiceHashMap = new HashMap<String, DynamicService>();

    /**
     * @return
     * @throws
     * @author intsmaze
     * @description: https://www.cnblogs.com/intsmaze/
     * 该java类在redis中key名称为"classload:"+packageName，在本地生成的class文件位于CLASS_PATH
     * @date : 2020/6/6 18:51
     * @Param
     */
    public boolean executeCompile(String fileSource, String className, String packageName) {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        JavaFileObject fileObject = (JavaFileObject) new JavaStringObject(className, fileSource);
        // 编译过程
        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, null, null,
                Arrays.asList("-d", CLASS_PATH), null,
                Arrays.asList(fileObject));
        if (!task.call()) {
            System.out.println("编译失败!");
            return false;
        } else {
            System.out.println("编译成功！");
            loadClassToRedis(packageName);
            return true;
        }
    }

    public boolean loadClassToRedis(String packageName) {
        FileSystemClassLoader fscl = new FileSystemClassLoader(
                CompileJavaFileRedisTemplate.CLASS_PATH);
        fileSystemClassLoader.setRootDir(CompileJavaFileRedisTemplate.CLASS_PATH);

        byte[] classData = fileSystemClassLoader.getClassData(packageName);
        Jedis jedis = jedisPool.getResource();
        jedis.set((KEY_NAME + packageName).getBytes(), classData);
        jedis.close();
        return true;
    }

    static class JavaStringObject extends SimpleJavaFileObject {
        private String code;

        public JavaStringObject(String name, String code) {
            super(URI.create(name + ".java"), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    public Map<String, DynamicService> getDynamicServiceHashMap() {
        return dynamicServiceHashMap;
    }

    public void setDynamicServiceHashMap(Map<String, DynamicService> dynamicServiceHashMap) {
        this.dynamicServiceHashMap = dynamicServiceHashMap;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public FileSystemClassLoader getFileSystemClassLoader() {
        return fileSystemClassLoader;
    }

    public void setFileSystemClassLoader(FileSystemClassLoader fileSystemClassLoader) {
        this.fileSystemClassLoader = fileSystemClassLoader;
    }


}

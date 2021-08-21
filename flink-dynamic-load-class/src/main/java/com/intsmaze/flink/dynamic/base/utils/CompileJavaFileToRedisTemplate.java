package com.intsmaze.flink.dynamic.base.utils;

import com.intsmaze.flink.dynamic.base.FileSystemClassLoader;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.net.URI;
import java.util.Arrays;

/**
 * @author intsmaze
 * @description: https://www.cnblogs.com/intsmaze/
 * 该类负责将编写的java类编译为class文件后加载进redis中
 * @date : 2020/6/6 18:49
 */
public class CompileJavaFileToRedisTemplate {

    private JedisPool jedisPool;

    private FileSystemClassLoader fileSystemClassLoader;

    public static final String CLASS_PATH = "c:/home/intsmaze/classload";

    public static final String KEY_NAME = "classload:";

    /**
     * @author intsmaze
     * @description: https://www.cnblogs.com/intsmaze/
     * 该java类在redis中key名称为"classload:"+packageName，在本地生成的class文件位于CLASS_PATH
     * @date : 2020/6/6 18:51
     * @Param
     * @return
     * @throws
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
            loadClassRedis(packageName);
            return true;
        }
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

    public boolean loadClassRedis(String packageName) {
		FileSystemClassLoader fscl = new FileSystemClassLoader(
                CompileJavaFileToRedisTemplate.CLASS_PATH);
        fileSystemClassLoader.setRootDir(CompileJavaFileToRedisTemplate.CLASS_PATH);

        byte[] classData = fileSystemClassLoader.getClassData(packageName);
        Jedis jedis = jedisPool.getResource();
        jedis.set((KEY_NAME + packageName).getBytes(), classData);
        return true;
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

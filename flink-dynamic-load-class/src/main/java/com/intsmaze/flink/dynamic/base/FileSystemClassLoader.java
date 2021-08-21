package com.intsmaze.flink.dynamic.base;

import com.intsmaze.flink.dynamic.base.utils.CompileJavaFileToRedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;

/**
 * @author intsmaze
 * @description: https://www.cnblogs.com/intsmaze/
 * @date : 2020/6/6 18:40
 */
public class FileSystemClassLoader extends ClassLoader {

    private JedisPool jedisPool;

    public String rootDir;

    public FileSystemClassLoader() {
    }

    public FileSystemClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }

    /**
     * @return
     * @throws
     * @author intsmaze
     * @description: https://www.cnblogs.com/intsmaze/
     * 从redis中获取指定类的字节码
     * @date : 2020/6/6 18:40
     * @Param 类的完整名称
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
//        byte[] classData = getClassData(name);  // 获取类的字节数组
        byte[] classData = getClassDataFormRedis(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(name, classData, 0, classData.length);
        }
    }

    private byte[] getClassDataFormRedis(String className) {
        Jedis jedis = jedisPool.getResource();
        return jedis.get((CompileJavaFileToRedisTemplate.KEY_NAME + className).getBytes());

    }

    public byte[] getClassData(String className) {
        // 读取类文件的字节
        String path = classNameToPath(className);
        try {
            InputStream ins = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesNumRead = 0;
            // 读取类文件的字节码
            while ((bytesNumRead = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesNumRead);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String classNameToPath(String className) {
        // 得到类文件的完全路径
        return rootDir + File.separatorChar
                + className.replace('.', File.separatorChar) + ".class";
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }
}

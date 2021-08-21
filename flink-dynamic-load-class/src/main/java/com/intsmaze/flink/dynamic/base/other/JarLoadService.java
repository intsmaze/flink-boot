package com.intsmaze.flink.dynamic.base.other;



import com.intsmaze.flink.dynamic.DynamicService;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


/** 
 * @author:YangLiu
 * @date:2018年8月8日 上午10:36:10 
 * @describe: 使用类加载器加载jar包，然后加载指定的类进去虚拟机，将intsmaze-classloadjar这个model打包
 */
public class JarLoadService {

	
	public void init()
	{
		try {
			String path = System.getProperty("user.dir");
			System.out.println(path);
			
			String[] jarsName=getJarsName("path");
			URL[] us =makeUrl(jarsName);
			ClassLoader loader = new URLClassLoader(us);
			// 输出类装载器的类型
			System.out.println(JarLoadService.class.getClassLoader());
			
			Class c = loader.loadClass("org.intsmaze.classload.service.impl.URLClassLoaderJar");
			DynamicService o = (DynamicService) c.newInstance();
			o.executeService("i am intsmaze");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	* @author:YangLiu
	* @date:2018年8月8日 上午10:48:11 
	* @describe:模拟根据文件夹得到该文件夹下所有文件的名称
	 */
	public String[] getJarsName(String path)
	{
        String[] arr = {"file:d:/intsmaze-classloadjar.jar", "file:d:/intsmaze-classloadjar.jar"};
		return arr;
	}
	
	/**
	* @author:YangLiu
	* @date:2018年8月8日 上午10:50:19 
	* @describe:模拟根据文件名称构造url数组
	 */
	public URL[] makeUrl(String[] arr) throws MalformedURLException
	{
		URL[] us = {new URL("file:d:/intsmaze-classloadjar.jar"),new URL("file:d:/intsmaze-classloadjar-v1.jar")};
		return us;
	}
	
	public static void main(String[] args) throws Exception {
		new JarLoadService().init();
	}
}

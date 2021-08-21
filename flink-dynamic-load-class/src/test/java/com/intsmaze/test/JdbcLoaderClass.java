package com.intsmaze.test;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

/**
 * @author:YangLiu
 * @date:2018年5月18日 下午2:01:27
 * @describe:
 */
public class JdbcLoaderClass {

	private static Connection conn;

	public static Connection getConn(String url, String user, String pass)
			throws Exception {
		if (conn == null) {
			URL[] urls = { new URL("file:d:/mysql-connector-java-5.1.30-bin.jar") };
			//通过这个标准的示例，我们可以发现，mysql-connector-java-5.1.30-bin.jar包中也没有打入java.sql.Driver，public class Driver extends NonRegisteringDriver implements java.sql.Driver {
			//把这个jar包加载进jvm后，只要jvm中已经加载进java.sql.Driver类即可。
			URLClassLoader myClassLoader = new URLClassLoader(urls);

			Driver driver = (Driver) myClassLoader.loadClass(
					"com.mysql.jdbc.Driver").newInstance();

			Properties props = new Properties();
			props.setProperty("user", user);
			props.setProperty("password", pass);

			conn = driver.connect(url, props);
		}
		return conn;

	}

	public static void main(String[] args) throws Exception {
		System.out.println(getConn("", "root", "root"));
	}

}

package com.intsmaze.flink.base.env;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.net.URL;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class BeanFactory extends DefaultListableBeanFactory {

	public static Logger LOG = LoggerFactory.getLogger(BeanFactory.class);

	public static final String SPRING_BEAN_FACTORY_XML = "springframework.bean.factory.xml";

	public static final String SPRING_BEAN_FACTORY_NAME = "springframework.bean.factory.name";

	private static BeanFactory instance;

	public static BeanFactory getInstance() {
		return instance;
	}

	public static void setInstance(BeanFactory instance) {
		BeanFactory.instance = instance;
	}

	private transient final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);

	private String xml;

	/**
	 * github地址: https://github.com/intsmaze
	 * 博客地址：https://www.cnblogs.com/intsmaze/
	 * 出版书籍《深入理解Flink核心设计与实践原理》
	 *
	 * @auther: intsmaze(刘洋)
	 * @date: 2020/10/15 18:33
	 */
	public BeanFactory(String location) {
		super();
		try {
			Resource resource = reader.getResourceLoader().getResource(location);
			URL url = resource.getURL();
			this.xml = Resources.toString(url, Charsets.UTF_8);
			this.reader.setValidating(false);
			this.reader.loadBeanDefinitions(resource);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * github地址: https://github.com/intsmaze
	 * 博客地址：https://www.cnblogs.com/intsmaze/
	 * 出版书籍《深入理解Flink核心设计与实践原理》
	 *
	 * @auther: intsmaze(刘洋)
	 * @date: 2020/10/15 18:33
	 */
	public BeanFactory(Configuration stormConf) {
		this.xml = stormConf.getString(SPRING_BEAN_FACTORY_XML,null);
		System.out.println("----->>>>> xml content is :"+xml);
		LOG.info("----->>>>> xml content is :{}"+xml);
		this.reader.setValidating(false);
		this.reader.loadBeanDefinitions(new ByteArrayResource(xml.getBytes(Charsets.UTF_8)));

	}

	/**
	 * github地址: https://github.com/intsmaze
	 * 博客地址：https://www.cnblogs.com/intsmaze/
	 * 出版书籍《深入理解Flink核心设计与实践原理》
	 *
	 * @auther: intsmaze(刘洋)
	 * @date: 2020/10/15 18:33
	 */
	public static ApplicationContext getBeanFactory(Configuration globalJobParameters) {
		String xmlName = globalJobParameters.getString(BeanFactory.SPRING_BEAN_FACTORY_NAME, null);
		System.out.println("xmlName is :"+xmlName);
		ApplicationContext beanConf = new ClassPathXmlApplicationContext(xmlName);
		return beanConf;
	}


	/**
	 * github地址: https://github.com/intsmaze
	 * 博客地址：https://www.cnblogs.com/intsmaze/
	 * 出版书籍《深入理解Flink核心设计与实践原理》
	 *
	 * @auther: intsmaze(刘洋)
	 * @date: 2020/10/15 18:33
	 */
	public String getXml() {
		return xml;
	}



}

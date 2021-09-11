package com.intsmaze.flink.groovy.bean;

import java.io.Serializable;

public class  GroovyDemo implements Serializable {

	public String getAge(String age)
	{
		return "39.9元配置文件带走:"+age;
	}
	
	public String getSex(String sex)
	{
		return "请我喝一杯星巴克都不愿意吗？"+sex;
	}
}

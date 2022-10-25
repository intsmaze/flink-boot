package com.intsmaze.flink.groovy;

import java.io.Serializable;

public class GroovyInterface implements Serializable {

	public String getAge(String age)
	{
		return "88.8元配置文件带走:"+age;
	}
	
	public String getSex(String sex)
	{
		return "请我喝一杯星巴克都不愿意吗？"+sex;
	}
}

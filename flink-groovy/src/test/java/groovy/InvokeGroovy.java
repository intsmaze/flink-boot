package groovy;

import groovy.lang.GroovyClassLoader;

/**
 * @author:YangLiu
 * @date:2018年5月18日 上午10:17:27
 * @describe:使用Groovy或 Jruby动态语言脚本作为规则引擎解决方案将会越来越普及.
 */
public class InvokeGroovy {
	public static void main(String[] args) {
		ClassLoader cl = new InvokeGroovy().getClass().getClassLoader();
		GroovyClassLoader groovyCl = new GroovyClassLoader(cl);
		try {
			// 从文件中读取
			// Class groovyClass = groovyCl.parseClass(new
			// File("D:/project/openjweb/src/java/org/openjweb/groovy/Foo.groovy"));
			// 直接使用Groovy字符串,也可以获得正确结果
			// Class groovyClass =
			// groovyCl.parseClass("package org.openjweb.groovy; /r/n import org.openjweb.core.groovy.test.IFoo;/r/n class Foo implements IFoo {public Object run(Object foo) {return 23}}");
			Class groovyClass = groovyCl
					.parseClass("package org.intsmaze.groovy;"
							+ " class Foo implements IFoo {"
							+ "public Object run(Object obj) "
							+ "{return obj.toString()+\"1111111111\"}"
							+ "}");

			IFoo foo = (IFoo) groovyClass.newInstance();
			System.out.println(foo.run(new Integer(2)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

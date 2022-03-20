package com.intsmaze.test;

import com.intsmaze.flink.dynamic.Constant;
import com.intsmaze.flink.dynamic.base.utils.CompileJavaFileRedisTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CompileJavaFileToRedisTest {


    /**
     * @Description: 将编写的java类动态编译为class文件后，加载进redis中
     * @Author: intsmaze
     * @Date: 2019/1/8
     */
    public static void main(String[] args) {

        ApplicationContext ct = new ClassPathXmlApplicationContext("spring-classload.xml");

        String classContent = "package com.intsmaze.flink.dynamic.impl;" +
                "import com.intsmaze.flink.dynamic.DynamicService;" +
                "public class " + Constant.CLASS_NAME + " implements DynamicService {" +
                "    public void executeService(String json) {" +
                "        System.out.println(\"我经过了redis后得以执行，看我72变\");" +
                "    }" +
                "}";

        CompileJavaFileRedisTemplate compileJavaFileRedisTemplate = (CompileJavaFileRedisTemplate) ct.getBean("compileJavaFileRedisTemplate");

        compileJavaFileRedisTemplate.executeCompile(classContent, Constant.CLASS_NAME, Constant.PACKAGE_NAME + Constant.CLASS_NAME);


    }


}

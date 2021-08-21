package com.intsmaze.test;

import com.intsmaze.flink.dynamic.base.utils.CompileJavaFileToRedisTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CompileJavaFileToRedisTest {

    public static final String CLASS_NAME = "DynamicDefineClassOne";

    /**
     * @Description: 将编写的java类动态编译为class文件后，加载进redis中
     * @Author: intsmaze
     * @Date: 2019/1/8
     */
    public static void main(String[] args) {

        ApplicationContext ct = new ClassPathXmlApplicationContext("spring-classload.xml");

        String classContent = "package com.intsmaze.classload.service.impl;" +
                "import com.intsmaze.classload.service.DynamicService;" +
                "public class " + CLASS_NAME + " implements DynamicService {" +
                "    public void executeService(String json) {" +
                "        System.out.println(\"我经过了redis后得以执行，看我72变\");" +
                "    }" +
                "}";

        CompileJavaFileToRedisTemplate compileJavaFileToRedisTemplate = (CompileJavaFileToRedisTemplate) ct.getBean("compileJavaFileToRedisTemplate");

        compileJavaFileToRedisTemplate.executeCompile(classContent, CLASS_NAME, "com.intsmaze.classload.service.impl." + CLASS_NAME);


    }


}

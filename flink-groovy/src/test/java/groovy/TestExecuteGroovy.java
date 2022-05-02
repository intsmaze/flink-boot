package groovy;

import com.intsmaze.flink.base.env.BeanFactory;
import com.intsmaze.flink.groovy.service.CacheGroovyService;
import com.intsmaze.flink.groovy.service.GroovyRuleCalc;
import com.intsmaze.flink.groovy.service.GroovyScriptExecutor;
import groovy.lang.Binding;
import org.junit.Test;
import com.intsmaze.flink.groovy.GroovyInterface;

public class TestExecuteGroovy {


    @Test
    public void test() {
        BeanFactory beanFactory = new BeanFactory("spring-load-groovy.xml");
        CacheGroovyService cacheService = beanFactory.getBean(CacheGroovyService.class);
        GroovyScriptExecutor scriptExecutor = beanFactory.getBean(GroovyScriptExecutor.class);

        Binding shellContext = new Binding();
        shellContext.setVariable("AGE", "100");
        shellContext.setVariable("SEX", "男");
        shellContext.setVariable("GROOVY", new GroovyInterface());

        //*********************配置脚本核心计算处*******************
        GroovyRuleCalc.executeRuleCalcAll(scriptExecutor, shellContext); // 核心计算
        //*********************配置脚本核心计算处*******************
        while (true) {

        }
    }
}

package groovy;

import com.intsmaze.flink.base.env.BeanFactory;
import com.intsmaze.flink.groovy.CacheGroovyService;
import com.intsmaze.flink.groovy.GroovyRuleCalc;
import com.intsmaze.flink.groovy.GroovyScriptExecutor;
import groovy.lang.Binding;
import org.junit.Test;
import com.intsmaze.flink.groovy.bean.GroovyDemo;

public class ExecuteGroovy {


    @Test
    public void test() {
        BeanFactory beanFactory = new BeanFactory("redis-base.xml");
        CacheGroovyService cacheService = beanFactory.getBean(CacheGroovyService.class);
        cacheService.init();

        GroovyScriptExecutor scriptExecutor = beanFactory.getBean(GroovyScriptExecutor.class);

        Binding shellContext = new Binding();
        shellContext.setVariable("AGE", "100");
        shellContext.setVariable("GROOVY", new GroovyDemo());

        //*********************配置脚本核心计算处*******************
        GroovyRuleCalc calc = new GroovyRuleCalc();
        calc.executeRuleCalcAll(scriptExecutor, shellContext); // 核心计算
        //*********************配置脚本核心计算处*******************
        while (true) {

        }
    }
}

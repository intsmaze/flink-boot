package com.intsmaze.flink.validate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class ValidatorUtil {

    private static Validator validator = Validation.buildDefaultValidatorFactory()
            .getValidator();

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    public static <T> Map<String,StringBuffer> validate(T obj){
        Set<ConstraintViolation<T>> validatorSet = validator.validate(obj, Default.class);
        Map<String,StringBuffer> errorMap = null;
        if(validatorSet != null && validatorSet.size() >0 ){
            errorMap = new HashMap<String,StringBuffer>();
            String property = null;
            for(ConstraintViolation<T> cv : validatorSet){
                property = cv.getPropertyPath().toString();
                if(errorMap.get(property) != null){
                    errorMap.get(property).append("," + cv.getMessage());
                }else{
                    StringBuffer sb = new StringBuffer();
                    sb.append(cv.getMessage());
                    errorMap.put(property, sb);
                }
            }
        }
        return errorMap;
    }



}

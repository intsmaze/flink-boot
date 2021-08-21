package com.intsmaze.flink.dynamic;

/**
 * @author intsmaze
 * @description: https://www.cnblogs.com/intsmaze/
 * @date : 2020/6/6 18:42
 */
public interface DynamicService {

    /**
     * @return
     * @throws
     * @author intsmaze
     * @description: https://www.cnblogs.com/intsmaze/
     * 新增的java类，要实现该接口，业务方法写在该方法里面
     * @date : 2020/6/6 18:42
     * @Param json 模拟的参数，可以为null
     */
    public void executeService(String json);

}

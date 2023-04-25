package com.springcloud.base.log.utils;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/25 10:03
 */
public interface Caller {

    /**
     * 获得调用者 * * @return 调用者
     */
    Class<?> getCaller();

    /**
     * 获得调用者的调用者 * * @return 调用者的调用者
     */
    Class<?> getCallerCaller();

    /**
     * 获得调用者，指定第几级调用者 调用者层级关系： * * @param depth 层级。0表示{@link CallerUtil}本身，1表示调用{@link CallerUtil}的类，2表示调用者的调用者，依次类推 * @return 第几级调用者
     */
    Class<?> getCaller(int depth);

    /**
     * 是否被指定类调用 * * @param clazz 调用者类 * @return 是否被调用
     */
    boolean isCalledBy(Class<?> clazz);

}

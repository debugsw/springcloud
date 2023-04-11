package com.springcloud.base.core.result;

/**
 * @Author: ls
 * @Date: 2023/1/15 16:54
 * @Description: 获取data失败之后的回调
 **/
public interface ResultFallback<T> {
    /**
     * callback 异常回调
     *
     * @param result 返回值
     * @return 对象
     */
    T callback(Result<T> result);
}

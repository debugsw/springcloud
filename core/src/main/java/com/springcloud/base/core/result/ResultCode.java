package com.springcloud.base.core.result;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/1/28 10:46
 */
public class ResultCode {

    /**
     * 默认失败
     */
    public static final String DEFAULT_FAIL = "0";

    /**
     * 默认成功
     */
    public static final String DEFAULT_SUCCESS = "1";
    /**
     * 异常失败
     */
    public static final String UNKNOWN_FAIL = "2";


    /**
     * 服务未发现
     */
    public static final String EXCEPTION_SERVICE_NOT_FOUND = "300";

    /**
     * 服务调用超时
     */
    public static final String EXCEPTION_SERVICE_TIME_OUT = "301";

    /**
     * 没有找到当前登录用户
     */
    public static final String NOT_FOUND_CURRENT_LOGIN_PERMISSION = "9001";

    /**
     * 达到限流上线 流量超过限流上线
     */
    public static final String CURRENT_OUT_OF_BOUNDS = "600";
}

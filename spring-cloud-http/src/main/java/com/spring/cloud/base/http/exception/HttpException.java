package com.spring.cloud.base.http.exception;

import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description: HTTP异常
 * @Date: 2023/4/26 15:00
 */
public class HttpException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public HttpException(Throwable e) {
        super(e.getMessage(), e);
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public HttpException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public HttpException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public HttpException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

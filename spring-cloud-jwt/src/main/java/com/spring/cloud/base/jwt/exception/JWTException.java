package com.spring.cloud.base.jwt.exception;

import com.spring.cloud.base.utils.exception.ExceptionUtil;
import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/25 13:36
 */
public class JWTException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JWTException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public JWTException(String message) {
        super(message);
    }

    public JWTException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public JWTException(String message, Throwable cause) {
        super(message, cause);
    }

    public JWTException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public JWTException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

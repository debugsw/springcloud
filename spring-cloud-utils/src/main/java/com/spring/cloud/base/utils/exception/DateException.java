package com.spring.cloud.base.utils.exception;

import com.spring.cloud.base.utils.crypto.StrUtil;

/**
 * @Author: ls
 * @Description: 日期异常
 * @Date: 2023/4/13 16:11
 */
public class DateException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public DateException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public DateException(String message) {
		super(message);
	}

	public DateException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public DateException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DateException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}

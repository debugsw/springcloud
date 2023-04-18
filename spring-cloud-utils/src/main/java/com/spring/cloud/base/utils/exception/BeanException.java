package com.spring.cloud.base.utils.exception;


import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description: Bean异常
 * @Date: 2023/4/13 16:11
 */
public class BeanException extends RuntimeException {
	private static final long serialVersionUID = -8096998667745023423L;

	public BeanException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public BeanException(String message) {
		super(message);
	}

	public BeanException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public BeanException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BeanException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}

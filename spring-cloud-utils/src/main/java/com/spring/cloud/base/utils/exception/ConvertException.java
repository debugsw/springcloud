package com.spring.cloud.base.utils.exception;

import com.spring.cloud.base.utils.crypto.StrUtil;

/**
 * @Author: ls
 * @Description: 转换异常
 * @Date: 2023/4/13 16:11
 */
public class ConvertException extends RuntimeException {
	private static final long serialVersionUID = 4730597402855274362L;

	public ConvertException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public ConvertException(String message) {
		super(message);
	}

	public ConvertException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ConvertException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ConvertException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}

package com.spring.cloud.base.utils.exception;

import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description: 克隆异常
 * @Date: 2023/4/13 16:11
 */
public class CloneRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 6774837422188798989L;

	public CloneRuntimeException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public CloneRuntimeException(String message) {
		super(message);
	}

	public CloneRuntimeException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public CloneRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CloneRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}

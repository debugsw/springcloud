package com.spring.cloud.base.utils.exception;

import com.spring.cloud.base.utils.crypto.StrUtil;

/**
 * @Author: ls
 * @Description: 验证异常
 * @Date: 2023/4/13 16:11
 */
public class ValidateException extends StatefulException {
	private static final long serialVersionUID = 6057602589533840889L;

	public ValidateException() {
	}

	public ValidateException(String msg) {
		super(msg);
	}

	public ValidateException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ValidateException(Throwable throwable) {
		super(throwable);
	}

	public ValidateException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public ValidateException(int status, String msg) {
		super(status, msg);
	}

	public ValidateException(int status, Throwable throwable) {
		super(status, throwable);
	}

	public ValidateException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public ValidateException(int status, String msg, Throwable throwable) {
		super(status, msg, throwable);
	}
}

package com.spring.cloud.base.utils.exception;

import com.spring.cloud.base.utils.crypto.StrUtil;
/**
 * @Author: ls
 * @Description: 加密异常
 * @Date: 2023/4/13 16:11
 */
public class CryptoException extends RuntimeException {
	private static final long serialVersionUID = 8068509879445395353L;

	public CryptoException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public CryptoException(String message) {
		super(message);
	}

	public CryptoException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public CryptoException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CryptoException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public CryptoException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}

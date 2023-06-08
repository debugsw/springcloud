package com.spring.cloud.base.jwt.json;

import com.spring.cloud.base.utils.exception.ExceptionUtil;
import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description: JSON异常
 * @Date: 2023/4/25 13:36
 */
public class JSONException extends RuntimeException {

	private static final long serialVersionUID = 0;

	public JSONException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public JSONException(String message) {
		super(message);
	}

	public JSONException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public JSONException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSONException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public JSONException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}

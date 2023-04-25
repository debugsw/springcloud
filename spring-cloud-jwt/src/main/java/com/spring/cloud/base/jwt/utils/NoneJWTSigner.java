package com.spring.cloud.base.jwt.utils;

import com.spring.cloud.base.jwt.JWTSigner;
import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description: 无需签名的JWT签名器
 * @Date: 2023/4/25 11:29
 */
public class NoneJWTSigner implements JWTSigner {

	public static final String ID_NONE = "none";

	public static NoneJWTSigner NONE = new NoneJWTSigner();

	@Override
	public String sign(String headerBase64, String payloadBase64) {
		return StrUtil.EMPTY;
	}

	@Override
	public boolean verify(String headerBase64, String payloadBase64, String signBase64) {
		return StrUtil.isEmpty(signBase64);
	}

	@Override
	public String getAlgorithm() {
		return ID_NONE;
	}
}

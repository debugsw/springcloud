package com.spring.cloud.base.jwt;

import java.util.Map;

/**
 * @Author: ls
 * @Description: JWT载荷信息
 * @Date: 2023/4/25 11:29
 */
public class JWTPayload extends Claims implements RegisteredPayload<JWTPayload> {

	private static final long serialVersionUID = 1L;

	/**
	 * 增加自定义JWT认证载荷信息
	 *
	 * @param payloadClaims 载荷信息
	 * @return this
	 */
	public JWTPayload addPayloads(Map<String, ?> payloadClaims) {
		putAll(payloadClaims);
		return this;
	}

	@Override
	public JWTPayload setPayload(String name, Object value) {
		setClaim(name, value);
		return this;
	}
}

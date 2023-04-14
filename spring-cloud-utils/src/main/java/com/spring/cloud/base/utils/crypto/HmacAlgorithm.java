package com.spring.cloud.base.utils.crypto;

/**
 * @Author: ls
 * @Description: HMAC算法类型
 * @Date: 2023/4/13 15:56
 */
public enum HmacAlgorithm {
	HmacMD5("HmacMD5"),
	HmacSHA1("HmacSHA1"),
	HmacSHA256("HmacSHA256"),
	HmacSHA384("HmacSHA384"),
	HmacSHA512("HmacSHA512"),
	/**
	 * HmacSM3算法实现，需要BouncyCastle库支持
	 */
	HmacSM3("HmacSM3"),
	/**
	 * SM4 CMAC模式实现，需要BouncyCastle库支持
	 */
	SM4CMAC("SM4CMAC");

	private final String value;

	HmacAlgorithm(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}

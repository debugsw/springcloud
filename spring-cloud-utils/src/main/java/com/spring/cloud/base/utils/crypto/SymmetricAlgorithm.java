package com.spring.cloud.base.utils.crypto;

/**
 * @Author: ls
 * @Description: 对称算法类型
 * @Date: 2023/4/13 15:56
 */
public enum SymmetricAlgorithm {
	/**
	 * 默认的AES加密方式：AES/ECB/PKCS5Padding
	 */
	AES("AES"),
	ARCFOUR("ARCFOUR"),
	Blowfish("Blowfish"),
	/**
	 * 默认的DES加密方式：DES/ECB/PKCS5Padding
	 */
	DES("DES"),
	/**
	 * 3DES算法，默认实现为：DESede/ECB/PKCS5Padding
	 */
	DESede("DESede"),
	RC2("RC2"),

	PBEWithMD5AndDES("PBEWithMD5AndDES"),
	PBEWithSHA1AndDESede("PBEWithSHA1AndDESede"),
	PBEWithSHA1AndRC2_40("PBEWithSHA1AndRC2_40");

	private final String value;

	/**
	 * 构造
	 *
	 * @param value 算法的字符串表示，区分大小写
	 */
	SymmetricAlgorithm(String value) {
		this.value = value;
	}

	/**
	 * 获得算法的字符串表示形式
	 *
	 * @return 算法字符串
	 */
	public String getValue() {
		return this.value;
	}
}

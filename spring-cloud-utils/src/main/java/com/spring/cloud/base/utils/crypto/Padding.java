package com.spring.cloud.base.utils.crypto;

/**
 * @Author: ls
 * @Description: 补码方式
 * @Date: 2023/4/13 15:56
 */
public enum Padding {
	/**
	 * 无补码
	 */
	NoPadding,
	/**
	 * 0补码，即不满block长度时使用0填充
	 */
	ZeroPadding,
	/**
	 * This padding for block ciphers is described in 5.2 Block Encryption Algorithms in the W3C's "XML Encryption Syntax and Processing" document.
	 */
	ISO10126Padding,
	/**
	 * Optimal Asymmetric Encryption Padding scheme defined in PKCS1
	 */
	OAEPPadding,
	/**
	 * The padding scheme described in PKCS #1, used with the RSA algorithm
	 */
	PKCS1Padding,
	/**
	 * The padding scheme described in RSA Laboratories, "PKCS #5: Password-Based Encryption Standard," version 1.5, November 1993.
	 */
	PKCS5Padding,
	/**
	 * The padding scheme defined in the SSL Protocol Version 3.0, November 18, 1996, section 5.2.3.2 (CBC block cipher)
	 */
	SSL3Padding
}

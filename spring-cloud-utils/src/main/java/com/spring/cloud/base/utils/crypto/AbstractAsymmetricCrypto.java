package com.spring.cloud.base.utils.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author: ls
 * @Description: 抽象的非对称加密对象
 * @Date: 2023/4/13 16:11
 */
public abstract class AbstractAsymmetricCrypto<T extends AbstractAsymmetricCrypto<T>>
		extends BaseAsymmetric<T>
		implements AsymmetricEncryptor, AsymmetricDecryptor {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  算法
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 */
	public AbstractAsymmetricCrypto(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		super(algorithm, privateKey, publicKey);
	}
}

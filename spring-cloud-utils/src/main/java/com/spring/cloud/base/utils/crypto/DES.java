package com.spring.cloud.base.utils.crypto;

import com.spring.cloud.base.utils.str.StrUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * @Author: ls
 * @Description: DES加密算法实现
 * @Date: 2023/4/13 15:56
 */
public class DES extends SymmetricCrypto {

	private static final long serialVersionUID = 1L;

	/**
	 * 构造，默认DES/ECB/PKCS5Padding，使用随机密钥
	 */
	public DES() {
		super(SymmetricAlgorithm.DES);
	}

	/**
	 * 构造，使用默认的DES/ECB/PKCS5Padding
	 *
	 * @param key 密钥
	 */
	public DES(byte[] key) {
		super(SymmetricAlgorithm.DES, key);
	}

	/**
	 * 构造，使用随机密钥
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public DES(Mode mode, Padding padding) {
		this(mode.name(), padding.name());
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，长度：8的倍数
	 */
	public DES(Mode mode, Padding padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，长度：8的倍数
	 * @param iv      偏移向量，加盐
	 */
	public DES(Mode mode, Padding padding, byte[] key, byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，长度：8的倍数
	 */
	public DES(Mode mode, Padding padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，长度：8的倍数
	 * @param iv      偏移向量，加盐
	 * 
	 */
	public DES(Mode mode, Padding padding, SecretKey key, IvParameterSpec iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 */
	public DES(String mode, String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，长度：8的倍数
	 */
	public DES(String mode, String padding, byte[] key) {
		this(mode, padding, SecureUtil.generateKey("DES", key), null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，长度：8的倍数
	 * @param iv      加盐
	 */
	public DES(String mode, String padding, byte[] key, byte[] iv) {
		this(mode, padding, SecureUtil.generateKey("DES", key), null == iv ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，长度：8的倍数
	 */
	public DES(String mode, String padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，长度：8的倍数
	 * @param iv      加盐
	 */
	public DES(String mode, String padding, SecretKey key, IvParameterSpec iv) {
		super(StrUtil.format("DES/{}/{}", mode, padding), key, iv);
	}
}

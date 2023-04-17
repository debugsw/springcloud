package com.spring.cloud.base.utils.crypto;

import com.spring.cloud.base.utils.str.StrUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * @Author: ls
 * @Description: DESede是由DES对称加密算法改进后的一种对称加密算法
 * @Date: 2023/4/13 15:56
 */
public class DESede extends SymmetricCrypto {

	private static final long serialVersionUID = 1L;

	/**
	 * 构造，默认DESede/ECB/PKCS5Padding，使用随机密钥
	 */
	public DESede() {
		super(SymmetricAlgorithm.DESede);
	}

	/**
	 * 构造，使用默认的DESede/ECB/PKCS5Padding
	 * 
	 * @param key 密钥
	 */
	public DESede(byte[] key) {
		super(SymmetricAlgorithm.DESede, key);
	}

	/**
	 * 构造，使用随机密钥
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public DESede(Mode mode, Padding padding) {
		this(mode.name(), padding.name());
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 */
	public DESede(Mode mode, Padding padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public DESede(Mode mode, Padding padding, byte[] key, byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 * @since 3.3.0
	 */
	public DESede(Mode mode, Padding padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public DESede(Mode mode, Padding padding, SecretKey key, IvParameterSpec iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 */
	public DESede(String mode, String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度24位
	 */
	public DESede(String mode, String padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度24位
	 * @param iv 加盐
	 */
	public DESede(String mode, String padding, byte[] key, byte[] iv) {
		this(mode, padding, SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue(), key), null == iv ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥
	 */
	public DESede(String mode, String padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥
	 * @param iv 加盐
	 */
	public DESede(String mode, String padding, SecretKey key, IvParameterSpec iv) {
		super(StrUtil.format("{}/{}/{}", SymmetricAlgorithm.DESede.getValue(), mode, padding), key, iv);
	}
}

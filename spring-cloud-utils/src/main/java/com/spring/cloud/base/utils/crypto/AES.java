package com.spring.cloud.base.utils.crypto;

import com.spring.cloud.base.utils.utils.ArrayUtil;
import com.spring.cloud.base.utils.str.StrUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @Author: ls
 * @Description: AES加密算法实现
 * @Date: 2023/4/13 16:11
 */
public class AES extends SymmetricCrypto {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造，默认AES/ECB/PKCS5Padding，使用随机密钥
	 */
	public AES() {
		super(SymmetricAlgorithm.AES);
	}

	/**
	 * 构造，使用默认的AES/ECB/PKCS5Padding
	 *
	 * @param key 密钥
	 */
	public AES(byte[] key) {
		super(SymmetricAlgorithm.AES, key);
	}

	/**
	 * 构造，使用默认的AES/ECB/PKCS5Padding
	 *
	 * @param key 密钥
	 */
	public AES(SecretKey key) {
		super(SymmetricAlgorithm.AES, key);
	}

	/**
	 * 构造，使用随机密钥
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public AES(Mode mode, Padding padding) {
		this(mode.name(), padding.name());
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(Mode mode, Padding padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      偏移向量，加盐
	 * 
	 */
	public AES(Mode mode, Padding padding, byte[] key, byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * 
	 */
	public AES(Mode mode, Padding padding, SecretKey key) {
		this(mode, padding, key, (IvParameterSpec) null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      偏移向量，加盐
	 * 
	 */
	public AES(Mode mode, Padding padding, SecretKey key, byte[] iv) {
		this(mode, padding, key, ArrayUtil.isEmpty(iv) ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode       模式{@link Mode}
	 * @param padding    {@link Padding}补码方式
	 * @param key        密钥，支持三种密钥长度：128、192、256位
	 * @param paramsSpec 算法参数，例如加盐等
	 * 
	 */
	public AES(Mode mode, Padding padding, SecretKey key, AlgorithmParameterSpec paramsSpec) {
		this(mode.name(), padding.name(), key, paramsSpec);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 */
	public AES(String mode, String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(String mode, String padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      加盐
	 */
	public AES(String mode, String padding, byte[] key, byte[] iv) {
		this(mode, padding,
				KeyUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key),
				ArrayUtil.isEmpty(iv) ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(String mode, String padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode       模式
	 * @param padding    补码方式
	 * @param key        密钥，支持三种密钥长度：128、192、256位
	 * @param paramsSpec 算法参数，例如加盐等
	 */
	public AES(String mode, String padding, SecretKey key, AlgorithmParameterSpec paramsSpec) {
		super(StrUtil.format("AES/{}/{}", mode, padding), key, paramsSpec);
	}
}

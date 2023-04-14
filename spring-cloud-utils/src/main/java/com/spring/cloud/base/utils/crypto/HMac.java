package com.spring.cloud.base.utils.crypto;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @Author: ls
 * @Description: HMAC摘要算法
 * @Date: 2023/4/13 15:56
 */
public class HMac extends Mac {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造，自动生成密钥
	 *
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 */
	public HMac(HmacAlgorithm algorithm) {
		this(algorithm, (Key) null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 * @param key       密钥
	 */
	public HMac(HmacAlgorithm algorithm, byte[] key) {
		this(algorithm.getValue(), key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 * @param key       密钥
	 */
	public HMac(HmacAlgorithm algorithm, Key key) {
		this(algorithm.getValue(), key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 4.5.13
	 */
	public HMac(String algorithm, byte[] key) {
		this(algorithm, new SecretKeySpec(key, algorithm));
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 4.5.13
	 */
	public HMac(String algorithm, Key key) {
		this(algorithm, key, null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @param spec      {@link AlgorithmParameterSpec}
	 * @since 5.6.12
	 */
	public HMac(String algorithm, Key key, AlgorithmParameterSpec spec) {
		this(MacEngineFactory.createEngine(algorithm, key, spec));
	}

	/**
	 * 构造
	 *
	 * @param engine MAC算法实现引擎
	 * @since 4.5.13
	 */
	public HMac(MacEngine engine) {
		super(engine);
	}
}

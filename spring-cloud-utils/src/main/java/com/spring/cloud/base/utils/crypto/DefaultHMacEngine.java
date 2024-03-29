package com.spring.cloud.base.utils.crypto;

import com.spring.cloud.base.utils.exception.CryptoException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @Author: ls
 * @Description: 默认的HMAC算法实现引擎
 * @Date: 2023/4/13 15:56
 */
public class DefaultHMacEngine implements MacEngine {

	private Mac mac;

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 */
	public DefaultHMacEngine(String algorithm, byte[] key) {
		this(algorithm, (null == key) ? null : new SecretKeySpec(key, algorithm));
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * 
	 */
	public DefaultHMacEngine(String algorithm, Key key) {
		this(algorithm, key, null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @param spec {@link AlgorithmParameterSpec}
	 * 
	 */
	public DefaultHMacEngine(String algorithm, Key key, AlgorithmParameterSpec spec) {
		init(algorithm, key, spec);
	}

	/**
	 * 初始化
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @return this
	 */
	public DefaultHMacEngine init(String algorithm, byte[] key) {
		return init(algorithm, (null == key) ? null : new SecretKeySpec(key, algorithm));
	}

	/**
	 * 初始化
	 *
	 * @param algorithm 算法
	 * @param key       密钥 {@link SecretKey}
	 * @return this
	 * @throws CryptoException Cause by IOException
	 */
	public DefaultHMacEngine init(String algorithm, Key key) {
		return init(algorithm, key, null);
	}

	/**
	 * 初始化
	 *
	 * @param algorithm 算法
	 * @param key       密钥 {@link SecretKey}
	 * @param spec      {@link AlgorithmParameterSpec}
	 * @return this
	 * @throws CryptoException Cause by IOException
	 * 
	 */
	public DefaultHMacEngine init(String algorithm, Key key, AlgorithmParameterSpec spec) {
		try {
			mac = SecureUtil.createMac(algorithm);
			if (null == key) {
				key = SecureUtil.generateKey(algorithm);
			}
			if (null != spec) {
				mac.init(key, spec);
			} else {
				mac.init(key);
			}
		} catch (Exception e) {
			throw new CryptoException(e);
		}
		return this;
	}

	/**
	 * 获得 {@link Mac}
	 *
	 * @return {@link Mac}
	 */
	public Mac getMac() {
		return mac;
	}

	@Override
	public void update(byte[] in) {
		this.mac.update(in);
	}

	@Override
	public void update(byte[] in, int inOff, int len) {
		this.mac.update(in, inOff, len);
	}

	@Override
	public byte[] doFinal() {
		return this.mac.doFinal();
	}

	@Override
	public void reset() {
		this.mac.reset();
	}

	@Override
	public int getMacLength() {
		return mac.getMacLength();
	}

	@Override
	public String getAlgorithm() {
		return this.mac.getAlgorithm();
	}
}

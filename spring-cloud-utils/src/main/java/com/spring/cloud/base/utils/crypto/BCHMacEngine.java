package com.spring.cloud.base.utils.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * @Author: ls
 * @Description: BouncyCastle的HMAC算法实现引擎实现摘要
 * @Date: 2023/4/13 16:11
 */
public class BCHMacEngine extends BCMacEngine {

	/**
	 * 构造
	 *
	 * @param digest 摘要算法，为{@link Digest} 的接口实现
	 * @param key    密钥
	 * @param iv     加盐
	 */
	public BCHMacEngine(Digest digest, byte[] key, byte[] iv) {
		this(digest, new ParametersWithIV(new KeyParameter(key), iv));
	}

	/**
	 * 构造
	 *
	 * @param digest 摘要算法，为{@link Digest} 的接口实现
	 * @param key    密钥
	 */
	public BCHMacEngine(Digest digest, byte[] key) {
		this(digest, new KeyParameter(key));
	}

	/**
	 * 构造
	 *
	 * @param digest 摘要算法
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 */
	public BCHMacEngine(Digest digest, CipherParameters params) {
		this(new HMac(digest), params);
	}

	/**
	 * 构造
	 *
	 * @param mac    {@link HMac}
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 */
	public BCHMacEngine(HMac mac, CipherParameters params) {
		super(mac, params);
	}

	/**
	 * 初始化
	 *
	 * @param digest 摘要算法
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @return this
	 * @see #init(Mac, CipherParameters)
	 */
	public BCHMacEngine init(Digest digest, CipherParameters params) {
		return (BCHMacEngine) init(new HMac(digest), params);
	}
}

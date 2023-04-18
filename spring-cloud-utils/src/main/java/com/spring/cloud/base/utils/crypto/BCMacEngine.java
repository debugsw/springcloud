package com.spring.cloud.base.utils.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * @Author: ls
 * @Description: BouncyCastle的MAC算法实现引擎实现摘要
 * @Date: 2023/4/13 16:11
 */
public class BCMacEngine implements MacEngine {

	private Mac mac;

	/**
	 * 构造
	 *
	 * @param mac    {@link Mac}
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @since 5.8.0
	 */
	public BCMacEngine(Mac mac, CipherParameters params) {
		init(mac, params);
	}

	/**
	 * 初始化
	 *
	 * @param mac    摘要算法
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @return this
	 * @since 5.8.0
	 */
	public BCMacEngine init(Mac mac, CipherParameters params) {
		mac.init(params);
		this.mac = mac;
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
	public void update(byte[] in, int inOff, int len) {
		this.mac.update(in, inOff, len);
	}

	@Override
	public byte[] doFinal() {
		final byte[] result = new byte[getMacLength()];
		this.mac.doFinal(result, 0);
		return result;
	}

	@Override
	public void reset() {
		this.mac.reset();
	}

	@Override
	public int getMacLength() {
		return mac.getMacSize();
	}

	@Override
	public String getAlgorithm() {
		return this.mac.getAlgorithmName();
	}
}

package com.spring.cloud.base.jwt.utils;

import com.spring.cloud.base.jwt.JWTSigner;
import com.spring.cloud.base.utils.utils.CharsetUtil;
import com.spring.cloud.base.utils.base.Base64;
import com.spring.cloud.base.utils.crypto.Sign;
import com.spring.cloud.base.utils.str.StrUtil;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author: ls
 * @Description: 非对称加密JWT签名封装
 * @Date: 2023/4/25 11:29
 */
public class AsymmetricJWTSigner implements JWTSigner {

	private Charset charset = CharsetUtil.CHARSET_UTF_8;
	private final Sign sign;

	/**
	 * 构造
	 *
	 * @param algorithm 算法字符串表示
	 * @param key       公钥{@link PublicKey}或私钥{@link PrivateKey}，公钥用于验证签名，私钥用于产生签名
	 */
	public AsymmetricJWTSigner(String algorithm, Key key) {
		final PublicKey publicKey = key instanceof PublicKey ? (PublicKey) key : null;
		final PrivateKey privateKey = key instanceof PrivateKey ? (PrivateKey) key : null;
		this.sign = new Sign(algorithm, privateKey, publicKey);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法字符串表示
	 * @param keyPair   密钥对
	 */
	public AsymmetricJWTSigner(String algorithm, KeyPair keyPair) {
		this.sign = new Sign(algorithm, keyPair);
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 * @return 编码
	 */
	public AsymmetricJWTSigner setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public String sign(String headerBase64, String payloadBase64) {
		return Base64.encodeUrlSafe(sign.sign(StrUtil.format("{}.{}", headerBase64, payloadBase64)));
	}

	@Override
	public boolean verify(String headerBase64, String payloadBase64, String signBase64) {
		return sign.verify(
				StrUtil.bytes(StrUtil.format("{}.{}", headerBase64, payloadBase64), charset),
				Base64.decode(signBase64));
	}

	@Override
	public String getAlgorithm() {
		return this.sign.getSignature().getAlgorithm();
	}

}

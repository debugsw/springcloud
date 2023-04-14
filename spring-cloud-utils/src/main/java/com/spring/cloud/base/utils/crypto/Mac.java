package com.spring.cloud.base.utils.crypto;

import com.spring.cloud.base.utils.*;
import com.spring.cloud.base.utils.exception.CryptoException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * @Author: ls
 * @Description: MAC摘要算法
 * @Date: 2023/4/13 15:56
 */
public class Mac implements Serializable {
	private static final long serialVersionUID = 1L;

	private final MacEngine engine;

	/**
	 * 构造
	 *
	 * @param engine MAC算法实现引擎
	 */
	public Mac(MacEngine engine) {
		this.engine = engine;
	}
	// ------------------------------------------------------------------------------------------- Constructor end

	/**
	 * 获得MAC算法引擎
	 *
	 * @return MAC算法引擎
	 */
	public MacEngine getEngine() {
		return this.engine;
	}

	// ------------------------------------------------------------------------------------------- Digest

	/**
	 * 生成文件摘要
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return 摘要
	 */
	public byte[] digest(String data, Charset charset) {
		return digest(StrUtil.bytes(data, charset));
	}

	/**
	 * 生成文件摘要
	 *
	 * @param data 被摘要数据
	 * @return 摘要
	 */
	public byte[] digest(String data) {
		return digest(data, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 生成文件摘要，并转为Base64
	 *
	 * @param data      被摘要数据
	 * @param isUrlSafe 是否使用URL安全字符
	 * @return 摘要
	 */
	public String digestBase64(String data, boolean isUrlSafe) {
		return digestBase64(data, CharsetUtil.CHARSET_UTF_8, isUrlSafe);
	}

	/**
	 * 生成文件摘要，并转为Base64
	 *
	 * @param data      被摘要数据
	 * @param charset   编码
	 * @param isUrlSafe 是否使用URL安全字符
	 * @return 摘要
	 */
	public String digestBase64(String data, Charset charset, boolean isUrlSafe) {
		final byte[] digest = digest(data, charset);
		return isUrlSafe ? Base64.encodeUrlSafe(digest) : Base64.encode(digest);
	}

	/**
	 * 生成文件摘要，并转为16进制字符串
	 *
	 * @param data    被摘要数据
	 * @param charset 编码
	 * @return 摘要
	 */
	public String digestHex(String data, Charset charset) {
		return HexUtil.encodeHexStr(digest(data, charset));
	}

	/**
	 * 生成文件摘要
	 *
	 * @param data 被摘要数据
	 * @return 摘要
	 */
	public String digestHex(String data) {
		return digestHex(data, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 生成文件摘要<br>
	 * 使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 *
	 * @param file 被摘要文件
	 * @return 摘要bytes
	 * @throws CryptoException Cause by IOException
	 */
	public byte[] digest(File file) throws CryptoException {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return digest(in);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 生成文件摘要，并转为16进制字符串<br>
	 * 使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 *
	 * @param file 被摘要文件
	 * @return 摘要
	 */
	public String digestHex(File file) {
		return HexUtil.encodeHexStr(digest(file));
	}

	/**
	 * 生成摘要
	 *
	 * @param data 数据bytes
	 * @return 摘要bytes
	 */
	public byte[] digest(byte[] data) {
		return digest(new ByteArrayInputStream(data), -1);
	}

	/**
	 * 生成摘要，并转为16进制字符串<br>
	 *
	 * @param data 被摘要数据
	 * @return 摘要
	 */
	public String digestHex(byte[] data) {
		return HexUtil.encodeHexStr(digest(data));
	}

	/**
	 * 生成摘要，使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 *
	 * @param data {@link InputStream} 数据流
	 * @return 摘要bytes
	 */
	public byte[] digest(InputStream data) {
		return digest(data, IoUtil.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 生成摘要，并转为16进制字符串<br>
	 * 使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 *
	 * @param data 被摘要数据
	 * @return 摘要
	 */
	public String digestHex(InputStream data) {
		return HexUtil.encodeHexStr(digest(data));
	}

	/**
	 * 生成摘要
	 *
	 * @param data         {@link InputStream} 数据流
	 * @param bufferLength 缓存长度，不足1使用 {@link IoUtil#DEFAULT_BUFFER_SIZE} 做为默认值
	 * @return 摘要bytes
	 */
	public byte[] digest(InputStream data, int bufferLength) {
		return this.engine.digest(data, bufferLength);
	}

	/**
	 * 生成摘要，并转为16进制字符串<br>
	 * 使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 *
	 * @param data         被摘要数据
	 * @param bufferLength 缓存长度，不足1使用 {@link IoUtil#DEFAULT_BUFFER_SIZE} 做为默认值
	 * @return 摘要
	 */
	public String digestHex(InputStream data, int bufferLength) {
		return HexUtil.encodeHexStr(digest(data, bufferLength));
	}

	/**
	 * 验证生成的摘要与给定的摘要比较是否一致<br>
	 * 简单比较每个byte位是否相同
	 *
	 * @param digest          生成的摘要
	 * @param digestToCompare 需要比较的摘要
	 * @return 是否一致
	 * @see MessageDigest#isEqual(byte[], byte[])
	 * @since 5.6.8
	 */
	public boolean verify(byte[] digest, byte[] digestToCompare) {
		return MessageDigest.isEqual(digest, digestToCompare);
	}

	/**
	 * 获取MAC算法块长度
	 *
	 * @return MAC算法块长度
	 * @since 5.3.3
	 */
	public int getMacLength() {
		return this.engine.getMacLength();
	}

	/**
	 * 获取算法
	 *
	 * @return 算法
	 * @since 5.3.3
	 */
	public String getAlgorithm() {
		return this.engine.getAlgorithm();
	}
}

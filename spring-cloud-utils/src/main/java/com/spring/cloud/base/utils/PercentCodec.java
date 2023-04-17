package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.str.StrUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.BitSet;

/**
 * @Author: ls
 * @Description: 百分号编码
 * @Date: 2023/4/13 16:11
 */
public class PercentCodec implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 从已知PercentCodec创建PercentCodec，会复制给定PercentCodec的安全字符
	 *
	 * @param codec PercentCodec
	 * @return PercentCodec
	 */
	public static PercentCodec of(PercentCodec codec) {
		return new PercentCodec((BitSet) codec.safeCharacters.clone());
	}

	/**
	 * 创建PercentCodec，使用指定字符串中的字符作为安全字符
	 *
	 * @param chars 安全字符合集
	 * @return PercentCodec
	 */
	public static PercentCodec of(CharSequence chars) {
		Assert.notNull(chars, "chars must not be null");
		final PercentCodec codec = new PercentCodec();
		final int length = chars.length();
		for (int i = 0; i < length; i++) {
			codec.addSafe(chars.charAt(i));
		}
		return codec;
	}

	/**
	 * 存放安全编码
	 */
	private final BitSet safeCharacters;

	/**
	 * 是否编码空格为+<br>
	 * 如果为{@code true}，则将空格编码为"+"，此项只在"application/x-www-form-urlencoded"中使用<br>
	 * 如果为{@code false}，则空格编码为"%20",此项一般用于URL的Query部分（RFC3986规范）
	 */
	private boolean encodeSpaceAsPlus = false;

	/**
	 * 构造<br>
	 * [a-zA-Z0-9]默认不被编码
	 */
	public PercentCodec() {
		this(new BitSet(256));
	}

	/**
	 * 构造
	 *
	 * @param safeCharacters 安全字符，安全字符不被编码
	 */
	public PercentCodec(BitSet safeCharacters) {
		this.safeCharacters = safeCharacters;
	}

	/**
	 * 增加安全字符<br>
	 * 安全字符不被编码
	 *
	 * @param c 字符
	 * @return this
	 */
	public PercentCodec addSafe(char c) {
		safeCharacters.set(c);
		return this;
	}

	/**
	 * 移除安全字符<br>
	 * 安全字符不被编码
	 *
	 * @param c 字符
	 * @return this
	 */
	public PercentCodec removeSafe(char c) {
		safeCharacters.clear(c);
		return this;
	}

	/**
	 * 增加安全字符到挡墙的PercentCodec
	 *
	 * @param codec PercentCodec
	 * @return this
	 */
	public PercentCodec or(PercentCodec codec) {
		this.safeCharacters.or(codec.safeCharacters);
		return this;
	}

	/**
	 * 组合当前PercentCodec和指定PercentCodec为一个新的PercentCodec，安全字符为并集
	 *
	 * @param codec PercentCodec
	 * @return 新的PercentCodec
	 */
	public PercentCodec orNew(PercentCodec codec) {
		return of(this).or(codec);
	}

	/**
	 * 是否将空格编码为+<br>
	 * 如果为{@code true}，则将空格编码为"+"，此项只在"application/x-www-form-urlencoded"中使用<br>
	 * 如果为{@code false}，则空格编码为"%20",此项一般用于URL的Query部分（RFC3986规范）
	 *
	 * @param encodeSpaceAsPlus 是否将空格编码为+
	 * @return this
	 */
	public PercentCodec setEncodeSpaceAsPlus(boolean encodeSpaceAsPlus) {
		this.encodeSpaceAsPlus = encodeSpaceAsPlus;
		return this;
	}

	/**
	 * 将URL中的字符串编码为%形式
	 *
	 * @param path           需要编码的字符串
	 * @param charset        编码, {@code null}返回原字符串，表示不编码
	 * @param customSafeChar 自定义安全字符
	 * @return 编码后的字符串
	 */
	public String encode(CharSequence path, Charset charset, char... customSafeChar) {
		if (null == charset || StrUtil.isEmpty(path)) {
			return StrUtil.str(path);
		}

		final StringBuilder rewrittenPath = new StringBuilder(path.length());
		final ByteArrayOutputStream buf = new ByteArrayOutputStream();
		final OutputStreamWriter writer = new OutputStreamWriter(buf, charset);

		char c;
		for (int i = 0; i < path.length(); i++) {
			c = path.charAt(i);
			if (safeCharacters.get(c) || ArrayUtil.contains(customSafeChar, c)) {
				rewrittenPath.append(c);
			} else if (encodeSpaceAsPlus && c == CharUtil.SPACE) {
				// 对于空格单独处理
				rewrittenPath.append('+');
			} else {
				// convert to external encoding before hex conversion
				try {
					writer.write(c);
					writer.flush();
				} catch (IOException e) {
					buf.reset();
					continue;
				}

				// 兼容双字节的Unicode符处理（如部分emoji）
				byte[] ba = buf.toByteArray();
				for (byte toEncode : ba) {
					// Converting each byte in the buffer
					rewrittenPath.append('%');
					HexUtil.appendHex(rewrittenPath, toEncode, false);
				}
				buf.reset();
			}
		}
		return rewrittenPath.toString();
	}
}

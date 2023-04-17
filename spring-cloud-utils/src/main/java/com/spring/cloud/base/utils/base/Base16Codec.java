package com.spring.cloud.base.utils.base;


import com.spring.cloud.base.utils.Decoder;
import com.spring.cloud.base.utils.Encoder;
import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.exception.UtilException;

/**
 * @Author: ls
 * @Description: Base16编码解码器
 * @Date: 2023/4/13 16:11
 */
public class Base16Codec implements Encoder<byte[], char[]>, Decoder<CharSequence, byte[]> {

	public static final Base16Codec CODEC_LOWER = new Base16Codec(true);
	public static final Base16Codec CODEC_UPPER = new Base16Codec(false);

	private final char[] alphabets;

	/**
	 * 构造
	 *
	 * @param lowerCase 是否小写
	 */
	public Base16Codec(boolean lowerCase) {
		this.alphabets = (lowerCase ? "0123456789abcdef" : "0123456789ABCDEF").toCharArray();
	}

	@Override
	public char[] encode(byte[] data) {
		final int len = data.length;
		final char[] out = new char[len << 1];
		// two characters from the hex value.
		for (int i = 0, j = 0; i < len; i++) {
			out[j++] = alphabets[(0xF0 & data[i]) >>> 4];
			out[j++] = alphabets[0x0F & data[i]];
		}
		return out;
	}

	@Override
	public byte[] decode(CharSequence encoded) {
		if (StrUtil.isEmpty(encoded)) {
			return null;
		}

		encoded = StrUtil.cleanBlank(encoded);
		int len = encoded.length();

		if ((len & 0x01) != 0) {
			// 如果提供的数据是奇数长度，则前面补0凑偶数
			encoded = "0" + encoded;
			len = encoded.length();
		}

		final byte[] out = new byte[len >> 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(encoded.charAt(j), j) << 4;
			j++;
			f = f | toDigit(encoded.charAt(j), j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}

	/**
	 * 将指定char值转换为Unicode字符串形式，常用于特殊字符（例如汉字）转Unicode形式<br>
	 * 转换的字符串如果u后不足4位，则前面用0填充，例如：
	 *
	 * <pre>
	 * '你' =》'\u4f60'
	 * </pre>
	 *
	 * @param ch char值
	 * @return Unicode表现形式
	 */
	public String toUnicodeHex(char ch) {
		return "\\u" +//
				alphabets[(ch >> 12) & 15] +//
				alphabets[(ch >> 8) & 15] +//
				alphabets[(ch >> 4) & 15] +//
				alphabets[(ch) & 15];
	}

	/**
	 * 将byte值转为16进制并添加到{@link StringBuilder}中
	 *
	 * @param builder {@link StringBuilder}
	 * @param b       byte
	 */
	public void appendHex(StringBuilder builder, byte b) {
		int high = (b & 0xf0) >>> 4;//高位
		int low = b & 0x0f;//低位
		builder.append(alphabets[high]);
		builder.append(alphabets[low]);
	}

	/**
	 * 将十六进制字符转换成一个整数
	 *
	 * @param ch    十六进制char
	 * @param index 十六进制字符在字符数组中的位置
	 * @return 一个整数
	 * @throws UtilException 当ch不是一个合法的十六进制字符时，抛出运行时异常
	 */
	private static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit < 0) {
			throw new UtilException("Illegal hexadecimal character {} at index {}", ch, index);
		}
		return digit;
	}
}

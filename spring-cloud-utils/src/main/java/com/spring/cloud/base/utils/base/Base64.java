package com.spring.cloud.base.utils.base;

import com.spring.cloud.base.utils.utils.CharsetUtil;
import com.spring.cloud.base.utils.utils.FileUtil;
import com.spring.cloud.base.utils.utils.IoUtil;
import com.spring.cloud.base.utils.str.StrUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
/**
 * @Author: ls
 * @Description: Base64工具类
 * @Date: 2023/4/13 16:11
 */
public class Base64 {

	private static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;

	/**
	 * 编码为Base64，非URL安全的
	 *
	 * @param arr     被编码的数组
	 * @param lineSep 在76个char之后是CRLF还是EOF
	 * @return 编码后的bytes
	 */
	public static byte[] encode(byte[] arr, boolean lineSep) {
		return lineSep ?
				java.util.Base64.getMimeEncoder().encode(arr) :
				java.util.Base64.getEncoder().encode(arr);
	}

	/**
	 * 编码为Base64，URL安全的
	 *
	 * @param arr     被编码的数组
	 * @param lineSep 在76个char之后是CRLF还是EOF
	 * @return 编码后的bytes
	 *
	 * @deprecated 按照RFC2045规范，URL安全的Base64无需换行
	 */
	@Deprecated
	public static byte[] encodeUrlSafe(byte[] arr, boolean lineSep) {
		return Base64Encoder.encodeUrlSafe(arr, lineSep);
	}

	/**
	 * base64编码
	 *
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(CharSequence source) {
		return encode(source, DEFAULT_CHARSET);
	}

	/**
	 * base64编码，URL安全
	 *
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String encodeUrlSafe(CharSequence source) {
		return encodeUrlSafe(source, DEFAULT_CHARSET);
	}

	/**
	 * base64编码
	 *
	 * @param source  被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(CharSequence source, String charset) {
		return encode(source, CharsetUtil.charset(charset));
	}

	/**
	 * base64编码，不进行padding(末尾不会填充'=')
	 *
	 * @param source  被编码的base64字符串
	 * @param charset 编码
	 * @return 被加密后的字符串
	 */
	public static String encodeWithoutPadding(CharSequence source, String charset) {
		return encodeWithoutPadding(StrUtil.bytes(source, charset));
	}

	/**
	 * base64编码,URL安全
	 *
	 * @param source  被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 *
	 * @deprecated 请使用 {@link #encodeUrlSafe(CharSequence, Charset)}
	 */
	@Deprecated
	public static String encodeUrlSafe(CharSequence source, String charset) {
		return encodeUrlSafe(source, CharsetUtil.charset(charset));
	}

	/**
	 * base64编码
	 *
	 * @param source  被编码的base64字符串
	 * @param charset 字符集
	 * @return 被编码后的字符串
	 */
	public static String encode(CharSequence source, Charset charset) {
		return encode(StrUtil.bytes(source, charset));
	}

	/**
	 * base64编码，URL安全的
	 *
	 * @param source  被编码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 * 
	 */
	public static String encodeUrlSafe(CharSequence source, Charset charset) {
		return encodeUrlSafe(StrUtil.bytes(source, charset));
	}

	/**
	 * base64编码
	 *
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(byte[] source) {
		return java.util.Base64.getEncoder().encodeToString(source);
	}

	/**
	 * base64编码，不进行padding(末尾不会填充'=')
	 *
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 * 
	 */
	public static String encodeWithoutPadding(byte[] source) {
		return java.util.Base64.getEncoder().withoutPadding().encodeToString(source);
	}

	/**
	 * base64编码,URL安全的
	 *
	 * @param source 被编码的base64字符串
	 * @return 被加密后的字符串
	 * 
	 */
	public static String encodeUrlSafe(byte[] source) {
		return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(source);
	}

	/**
	 * base64编码
	 *
	 * @param in 被编码base64的流（一般为图片流或者文件流）
	 * @return 被加密后的字符串
	 * 
	 */
	public static String encode(InputStream in) {
		return encode(IoUtil.readBytes(in));
	}

	/**
	 * base64编码,URL安全的
	 *
	 * @param in 被编码base64的流（一般为图片流或者文件流）
	 * @return 被加密后的字符串
	 * 
	 */
	public static String encodeUrlSafe(InputStream in) {
		return encodeUrlSafe(IoUtil.readBytes(in));
	}

	/**
	 * base64编码
	 *
	 * @param file 被编码base64的文件
	 * @return 被加密后的字符串
	 * 
	 */
	public static String encode(File file) {
		return encode(FileUtil.readBytes(file));
	}

	/**
	 * base64编码,URL安全的
	 *
	 * @param file 被编码base64的文件
	 * @return 被加密后的字符串
	 * 
	 */
	public static String encodeUrlSafe(File file) {
		return encodeUrlSafe(FileUtil.readBytes(file));
	}

	/**
	 * 编码为Base64字符串<br>
	 * 如果isMultiLine为{@code true}，则每76个字符一个换行符，否则在一行显示
	 *
	 * @param arr         被编码的数组
	 * @param isMultiLine 在76个char之后是CRLF还是EOF
	 * @param isUrlSafe   是否使用URL安全字符，一般为{@code false}
	 * @return 编码后的bytes
	 * 
	 */
	public static String encodeStr(byte[] arr, boolean isMultiLine, boolean isUrlSafe) {
		return StrUtil.str(encode(arr, isMultiLine, isUrlSafe), DEFAULT_CHARSET);
	}

	/**
	 * 编码为Base64<br>
	 * 如果isMultiLine为{@code true}，则每76个字符一个换行符，否则在一行显示
	 *
	 * @param arr         被编码的数组
	 * @param isMultiLine 在76个char之后是CRLF还是EOF
	 * @param isUrlSafe   是否使用URL安全字符，一般为{@code false}
	 * @return 编码后的bytes
	 */
	public static byte[] encode(byte[] arr, boolean isMultiLine, boolean isUrlSafe) {
		return Base64Encoder.encode(arr, isMultiLine, isUrlSafe);
	}

	/**
	 * base64解码
	 *
	 * @param source 被解码的base64字符串
	 * @return 被加密后的字符串
	 * 
	 */
	public static String decodeStrGbk(CharSequence source) {
		return Base64Decoder.decodeStr(source, CharsetUtil.CHARSET_GBK);
	}

	/**
	 * base64解码
	 *
	 * @param source 被解码的base64字符串
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(CharSequence source) {
		return Base64Decoder.decodeStr(source);
	}

	/**
	 * base64解码
	 *
	 * @param source  被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(CharSequence source, String charset) {
		return decodeStr(source, CharsetUtil.charset(charset));
	}

	/**
	 * base64解码
	 *
	 * @param source  被解码的base64字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(CharSequence source, Charset charset) {
		return Base64Decoder.decodeStr(source, charset);
	}

	/**
	 * base64解码
	 *
	 * @param base64   被解码的base64字符串
	 * @param destFile 目标文件
	 * @return 目标文件
	 * 
	 */
	public static File decodeToFile(CharSequence base64, File destFile) {
		return FileUtil.writeBytes(Base64Decoder.decode(base64), destFile);
	}

	/**
	 * base64解码
	 *
	 * @param base64     被解码的base64字符串
	 * @param out        写出到的流
	 * @param isCloseOut 是否关闭输出流
	 * 
	 */
	public static void decodeToStream(CharSequence base64, OutputStream out, boolean isCloseOut) {
		IoUtil.write(out, isCloseOut, Base64Decoder.decode(base64));
	}

	/**
	 * base64解码
	 *
	 * @param base64 被解码的base64字符串
	 * @return 解码后的bytes
	 */
	public static byte[] decode(CharSequence base64) {
		return Base64Decoder.decode(base64);
	}

	/**
	 * 解码Base64
	 *
	 * @param in 输入
	 * @return 解码后的bytes
	 */
	public static byte[] decode(byte[] in) {
		return Base64Decoder.decode(in);
	}

	/**
	 * 检查是否为Base64
	 *
	 * @param base64 Base64的bytes
	 * @return 是否为Base64
	 * 
	 */
	public static boolean isBase64(CharSequence base64) {
		if (base64 == null || base64.length() < 2) {
			return false;
		}
		final byte[] bytes = StrUtil.utf8Bytes(base64);
		if (bytes.length != base64.length()) {
			return false;
		}
		return isBase64(bytes);
	}

	/**
	 * 检查是否为Base64
	 *
	 * @param base64Bytes Base64的bytes
	 * @return 是否为Base64
	 * 
	 */
	public static boolean isBase64(byte[] base64Bytes) {
		if (base64Bytes == null || base64Bytes.length < 3) {
			return false;
		}
		boolean hasPadding = false;
		for (byte base64Byte : base64Bytes) {
			if (hasPadding) {
				if ('=' != base64Byte) {
					return false;
				}
			} else if ('=' == base64Byte) {
				hasPadding = true;
			} else if (!(Base64Decoder.isBase64Code(base64Byte) || isWhiteSpace(base64Byte))) {
				return false;
			}
		}
		return true;
	}

	private static boolean isWhiteSpace(byte byteToCheck) {
		switch (byteToCheck) {
			case ' ':
			case '\n':
			case '\r':
			case '\t':
				return true;
			default:
				return false;
		}
	}
}

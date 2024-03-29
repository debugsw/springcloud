package com.spring.cloud.base.utils.utils;

import com.spring.cloud.base.utils.RFC3986;
import com.spring.cloud.base.utils.exception.UtilException;
import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.utils.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @Author: ls
 * @Description: URL编码工具
 * @Date: 2023/4/13 16:11
 */
public class URLEncodeUtil {
	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。
	 *
	 * @param url URL
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String encodeAll(String url) {
		return encodeAll(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码URL<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。
	 *
	 * @param url     URL
	 * @param charset 编码，为null表示不编码
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String encodeAll(String url, Charset charset) throws UtilException {
		return RFC3986.UNRESERVED.encode(url, charset);
	}

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
	 *
	 * @param url URL
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String encode(String url) throws UtilException {
		return encode(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码字符为 application/x-www-form-urlencoded<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
	 *
	 * @param url     被编码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 */
	public static String encode(String url, Charset charset) {
		return RFC3986.PATH.encode(url, charset);
	}

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于POST请求中的请求体自动编码，转义大部分特殊字符
	 *
	 * @param url URL
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 * 
	 */
	public static String encodeQuery(String url) throws UtilException {
		return encodeQuery(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码字符为URL中查询语句<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于POST请求中的请求体自动编码，转义大部分特殊字符
	 *
	 * @param url     被编码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 * 
	 */
	public static String encodeQuery(String url, Charset charset) {
		return RFC3986.QUERY.encode(url, charset);
	}

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL的Segment中自动编码，转义大部分特殊字符
	 *
	 * <pre>
	 * pchar = unreserved（不处理） / pct-encoded / sub-delims（子分隔符） / "@"
	 * unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 * sub-delims = "!" / "$" / "&amp;" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
	 * </pre>
	 *
	 * @param url URL
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 * 
	 */
	public static String encodePathSegment(String url) throws UtilException {
		return encodePathSegment(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码字符为URL中查询语句<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL的Segment中自动编码，转义大部分特殊字符
	 *
	 * <pre>
	 * pchar = unreserved / pct-encoded / sub-delims / ":" / "@"
	 * unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 * sub-delims = "!" / "$" / "&amp;" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
	 * </pre>
	 *
	 * @param url     被编码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 * 
	 */
	public static String encodePathSegment(String url, Charset charset) {
		if (StrUtil.isEmpty(url)) {
			return url;
		}
		return RFC3986.SEGMENT.encode(url, charset);
	}

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * URL的Fragment URLEncoder<br>
	 * 默认的编码器针对Fragment，定义如下：
	 *
	 * <pre>
	 * fragment    = *( pchar / "/" / "?" )
	 * pchar       = unreserved / pct-encoded / sub-delims / ":" / "@"
	 * unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 * sub-delims  = "!" / "$" / "&amp;" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
	 * </pre>
	 * <p>
	 * 具体见：https://datatracker.ietf.org/doc/html/rfc3986#section-3.5
	 *
	 * @param url 被编码内容
	 * @return 编码后的字符
	 * 
	 */
	public static String encodeFragment(String url) throws UtilException {
		return encodeFragment(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * URL的Fragment URLEncoder<br>
	 * 默认的编码器针对Fragment，定义如下：
	 *
	 * <pre>
	 * fragment    = *( pchar / "/" / "?" )
	 * pchar       = unreserved / pct-encoded / sub-delims / ":" / "@"
	 * unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 * sub-delims  = "!" / "$" / "&amp;" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
	 * </pre>
	 * <p>
	 * 具体见：https://datatracker.ietf.org/doc/html/rfc3986#section-3.5
	 *
	 * @param url     被编码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 * 
	 */
	public static String encodeFragment(String url, Charset charset) {
		if (StrUtil.isEmpty(url)) {
			return url;
		}
		return RFC3986.FRAGMENT.encode(url, charset);
	}
}

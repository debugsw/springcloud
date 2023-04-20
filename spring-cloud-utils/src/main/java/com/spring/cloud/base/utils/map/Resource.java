package com.spring.cloud.base.utils.map;

import com.spring.cloud.base.utils.CharsetUtil;
import com.spring.cloud.base.utils.IoUtil;
import com.spring.cloud.base.utils.exception.IORuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @Author: ls
 * @Description: 资源接口定义
 * @Date: 2023/4/13 16:11
 */
public interface Resource {

	/**
	 * 获取资源名，例如文件资源的资源名为文件名
	 *
	 * @return 资源名
	 */
	String getName();

	/**
	 * 获得解析后的{@link URL}，无对应URL的返回{@code null}
	 *
	 * @return 解析后的{@link URL}
	 */
	URL getUrl();

	/**
	 * 获得 {@link InputStream}
	 *
	 * @return {@link InputStream}
	 */
	InputStream getStream();

	/**
	 * 检查资源是否变更<br>
	 * 一般用于文件类资源，检查文件是否被修改过。
	 *
	 * @return 是否变更
	 * 
	 */
	default boolean isModified(){
		return false;
	}

	/**
	 * 将资源内容写出到流，不关闭输出流，但是关闭资源流
	 *
	 * @param out 输出流
	 * @throws IORuntimeException IO异常
	 * 
	 */
	default void writeTo(OutputStream out) throws IORuntimeException {
		try (InputStream in = getStream()) {
			IoUtil.copy(in, out);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得Reader
	 *
	 * @param charset 编码
	 * @return {@link BufferedReader}
	 */
	default BufferedReader getReader(Charset charset) {
		return IoUtil.getReader(getStream(), charset);
	}

	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 *
	 * @param charset 编码
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装{@link IOException}
	 */
	default String readStr(Charset charset) throws IORuntimeException {
		return IoUtil.read(getReader(charset));
	}

	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 *
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装IOException
	 */
	default String readUtf8Str() throws IORuntimeException {
		return readStr(CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 *
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装IOException
	 */
	default byte[] readBytes() throws IORuntimeException {
		return IoUtil.readBytes(getStream());
	}
}

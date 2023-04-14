package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.spring.cloud.base.utils.impl.ComputeIter;
import com.spring.cloud.base.utils.map.IterableIter;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @Author: ls
 * @Description: 将Reader包装为一个按照行读取的Iterator
 * @Date: 2023/4/13 16:11
 */
public class LineIter extends ComputeIter<String> implements IterableIter<String>, Closeable, Serializable {
	private static final long serialVersionUID = 1L;

	private final BufferedReader bufferedReader;

	/**
	 * 构造
	 *
	 * @param in      {@link InputStream}
	 * @param charset 编码
	 * @throws IllegalArgumentException reader为null抛出此异常
	 */
	public LineIter(InputStream in, Charset charset) throws IllegalArgumentException {
		this(IoUtil.getReader(in, charset));
	}

	/**
	 * 构造
	 *
	 * @param reader {@link Reader}对象，不能为null
	 * @throws IllegalArgumentException reader为null抛出此异常
	 */
	public LineIter(Reader reader) throws IllegalArgumentException {
		Assert.notNull(reader, "Reader must not be null");
		this.bufferedReader = IoUtil.getReader(reader);
	}

	// -----------------------------------------------------------------------
	@Override
	protected String computeNext() {
		try {
			while (true) {
				String line = bufferedReader.readLine();
				if (line == null) {
					return null;
				} else if (isValidLine(line)) {
					return line;
				}
				// 无效行，则跳过进入下一行
			}
		} catch (IOException ioe) {
			close();
			throw new IORuntimeException(ioe);
		}
	}

	/**
	 * 关闭Reader
	 */
	@Override
	public void close() {
		super.finish();
		IoUtil.close(bufferedReader);
	}

	/**
	 * 重写此方法来判断是否每一行都被返回，默认全部为true
	 *
	 * @param line 需要验证的行
	 * @return 是否通过验证
	 */
	protected boolean isValidLine(String line) {
		return true;
	}
}

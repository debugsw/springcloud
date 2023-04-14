package com.spring.cloud.base.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: ls
 * @Description: 此OutputStream写出数据到
 * @Date: 2023/4/13 16:11
 */
public class NullOutputStream extends OutputStream {

	/**
	 * 单例
	 */
	public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();

	/**
	 * 什么也不做，写出到<code>/dev/null</code>.
	 *
	 * @param b   写出的数据
	 * @param off 开始位置
	 * @param len 长度
	 */
	@Override
	public void write(byte[] b, int off, int len) {
		// to /dev/null
	}

	/**
	 * 什么也不做，写出到 <code>/dev/null</code>.
	 *
	 * @param b 写出的数据
	 */
	@Override
	public void write(int b) {
		// to /dev/null
	}

	/**
	 * 什么也不做，写出到 <code>/dev/null</code>.
	 *
	 * @param b 写出的数据
	 * @throws IOException 不抛出
	 */
	@Override
	public void write(byte[] b) throws IOException {
		// to /dev/null
	}

}

package com.spring.cloud.base.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: ls
 * @Description: 限制读取最大长度的
 * @Date: 2023/4/13 16:11
 */
public class LimitedInputStream extends FilterInputStream {

	private final long maxSize;
	private long currentPos;

	/**
	 * 构造
	 * @param in {@link InputStream}
	 * @param maxSize 限制最大读取量，单位byte
	 */
	public LimitedInputStream(InputStream in, long maxSize) {
		super(in);
		this.maxSize = maxSize;
	}

	@Override
	public int read() throws IOException {
		final int data = super.read();
		if (data != -1) {
			currentPos++;
			checkPos();
		}
		return data;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		final int count = super.read(b, off, len);
		if (count > 0) {
			currentPos += count;
			checkPos();
		}
		return count;
	}

	@Override
	public long skip(long n) throws IOException {
		final long skipped = super.skip(n);
		if (skipped != 0) {
			currentPos += skipped;
			checkPos();
		}
		return skipped;
	}

	private void checkPos() {
		if (currentPos > maxSize) {
			throw new IllegalStateException("Read limit exceeded");
		}
	}
}

package com.spring.cloud.base.utils.date;

import com.spring.cloud.base.utils.crypto.StrUtil;

/**
 * @Author: ls
 * @Description: 数据单位封装
 * @Date: 2023/4/13 16:11
 */
public enum DataUnit {

	/**
	 * Bytes, 后缀表示为： {@code B}.
	 */
	BYTES("B", DataSize.ofBytes(1)),

	/**
	 * Kilobytes, 后缀表示为： {@code KB}.
	 */
	KILOBYTES("KB", DataSize.ofKilobytes(1)),

	/**
	 * Megabytes, 后缀表示为： {@code MB}.
	 */
	MEGABYTES("MB", DataSize.ofMegabytes(1)),

	/**
	 * Gigabytes, 后缀表示为： {@code GB}.
	 */
	GIGABYTES("GB", DataSize.ofGigabytes(1)),

	/**
	 * Terabytes, 后缀表示为： {@code TB}.
	 */
	TERABYTES("TB", DataSize.ofTerabytes(1));

	public static final String[] UNIT_NAMES = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB"};

	private final String suffix;

	private final DataSize size;


	DataUnit(String suffix, DataSize size) {
		this.suffix = suffix;
		this.size = size;
	}

	DataSize size() {
		return this.size;
	}

	/**
	 * 通过后缀返回对应的 DataUnit
	 *
	 * @param suffix 单位后缀
	 * @return 匹配到的{@link DataUnit}
	 * @throws IllegalArgumentException 后缀无法识别报错
	 */
	public static DataUnit fromSuffix(String suffix) {
		for (DataUnit candidate : values()) {
			// 支持类似于 3MB，3M，3m等写法
			if (StrUtil.startWithIgnoreCase(candidate.suffix, suffix)) {
				return candidate;
			}
		}
		throw new IllegalArgumentException("Unknown data unit suffix '" + suffix + "'");
	}

}

package com.spring.cloud.base.utils.utils;

import com.spring.cloud.base.utils.date.DataSize;
import com.spring.cloud.base.utils.date.DataUnit;

import java.text.DecimalFormat;

/**
 * @Author: ls
 * @Description: 数据大小工具类
 * @Date: 2023/4/13 16:11
 */
public class DataSizeUtil {

	/**
	 * 解析数据大小字符串，转换为bytes大小
	 *
	 * @param text 数据大小字符串，类似于：12KB, 5MB等
	 * @return bytes大小
	 */
	public static long parse(String text) {
		return DataSize.parse(text).toBytes();
	}

	/**
	 * 可读的文件大小
	 *
	 * @param size Long类型大小
	 * @return 大小
	 */
	public static String format(long size) {
		if (size <= 0) {
			return "0";
		}
		int digitGroups = Math.min(DataUnit.UNIT_NAMES.length - 1, (int) (Math.log10(size) / Math.log10(1024)));
		return new DecimalFormat("#,##0.##")
				.format(size / Math.pow(1024, digitGroups)) + " " + DataUnit.UNIT_NAMES[digitGroups];
	}
}

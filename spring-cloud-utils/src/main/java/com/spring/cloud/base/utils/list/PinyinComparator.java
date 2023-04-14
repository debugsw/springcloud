package com.spring.cloud.base.utils.list;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * @Author: ls
 * @Description: 按照GBK拼音顺序对给定的汉字字符串排序
 * @Date: 2023/4/13 16:11
 */
public class PinyinComparator implements Comparator<String>, Serializable {
	private static final long serialVersionUID = 1L;

	final Collator collator;

	/**
	 * 构造
	 */
	public PinyinComparator() {
		collator = Collator.getInstance(Locale.CHINESE);
	}

	@Override
	public int compare(String o1, String o2) {
		return collator.compare(o1, o2);
	}

}

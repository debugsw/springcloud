package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.str.StrBuilder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: ls
 * @Description: 字符串替换链用于组合多个字符串替换逻辑
 * @Date: 2023/4/13 16:11
 */
public class ReplacerChain extends StrReplacer implements Chain<StrReplacer, ReplacerChain> {
	private static final long serialVersionUID = 1L;

	private final List<StrReplacer> replacers = new LinkedList<>();

	/**
	 * 构造
	 *
	 * @param strReplacers 字符串替换器
	 */
	public ReplacerChain(StrReplacer... strReplacers) {
		for (StrReplacer strReplacer : strReplacers) {
			addChain(strReplacer);
		}
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public Iterator<StrReplacer> iterator() {
		return replacers.iterator();
	}

	@Override
	public ReplacerChain addChain(StrReplacer element) {
		replacers.add(element);
		return this;
	}

	@Override
	protected int replace(CharSequence str, int pos, StrBuilder out) {
		int consumed = 0;
		for (StrReplacer strReplacer : replacers) {
			consumed = strReplacer.replace(str, pos, out);
			if (0 != consumed) {
				return consumed;
			}
		}
		return consumed;
	}

}

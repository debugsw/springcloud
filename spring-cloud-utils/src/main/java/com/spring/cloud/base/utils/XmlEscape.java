package com.spring.cloud.base.utils;

/**
 * @Author: ls
 * @Description: XML特殊字符转义
 * @Date: 2023/4/13 16:11
 */
public class XmlEscape extends ReplacerChain {
	private static final long serialVersionUID = 1L;

	protected static final String[][] BASIC_ESCAPE = {
			{"\"", "&quot;"},
			{"&", "&amp;"},
			{"<", "&lt;"},
			{">", "&gt;"},
	};

	/**
	 * 构造
	 */
	public XmlEscape() {
		addChain(new LookupReplacer(BASIC_ESCAPE));
	}
}

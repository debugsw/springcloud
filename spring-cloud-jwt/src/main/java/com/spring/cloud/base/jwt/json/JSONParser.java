package com.spring.cloud.base.jwt.json;

import com.spring.cloud.base.jwt.utils.MutablePair;
import com.spring.cloud.base.utils.base.Mutable;
import com.spring.cloud.base.utils.interf.Filter;

/**
 * @Author: ls
 * @Description: JSON字符串解析器
 * @Date: 2023/4/25 11:29
 */
public class JSONParser {

	/**
	 * 创建JSONParser
	 *
	 * @param tokener {@link JSONTokener}
	 * @return JSONParser
	 */
	public static JSONParser of(JSONTokener tokener) {
		return new JSONParser(tokener);
	}

	private final JSONTokener tokener;

	/**
	 * 构造
	 *
	 * @param tokener {@link JSONTokener}
	 */
	public JSONParser(JSONTokener tokener) {
		this.tokener = tokener;
	}

	/**
	 * 解析{@link JSONTokener}中的字符到目标的{@link JSONObject}中
	 *
	 * @param jsonObject {@link JSONObject}
	 * @param filter     键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤
	 */
	public void parseTo(JSONObject jsonObject, Filter<MutablePair<String, Object>> filter) {
		final JSONTokener tokener = this.tokener;
		if (tokener.nextClean() != '{') {
			throw tokener.syntaxError("A JSONObject text must begin with '{'");
		}
		char prev;
		char c;
		String key;
		while (true) {
			prev = tokener.getPrevious();
			c = tokener.nextClean();
			switch (c) {
				case 0:
					throw tokener.syntaxError("A JSONObject text must end with '}'");
				case '}':
					return;
				case '{':
				case '[':
					if (prev == '{') {
						throw tokener.syntaxError("A JSONObject can not directly nest another JSONObject or JSONArray.");
					}
				default:
					tokener.back();
					key = tokener.nextValue().toString();
			}
			c = tokener.nextClean();
			if (c != ':') {
				throw tokener.syntaxError("Expected a ':' after a key");
			}
			jsonObject.set(key, tokener.nextValue(), filter, jsonObject.getConfig().isCheckDuplicate());
			switch (tokener.nextClean()) {
				case ';':
				case ',':
					if (tokener.nextClean() == '}') {
						return;
					}
					tokener.back();
					break;
				case '}':
					return;
				default:
					throw tokener.syntaxError("Expected a ',' or '}'");
			}
		}
	}

	/**
	 * 解析JSON字符串到{@link JSONArray}中
	 *
	 * @param jsonArray {@link JSONArray}
	 * @param filter    键值对过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null} 表示不过滤
	 */
	public void parseTo(JSONArray jsonArray, Filter<Mutable<Object>> filter) {
		final JSONTokener x = this.tokener;
		if (x.nextClean() != '[') {
			throw x.syntaxError("A JSONArray text must start with '['");
		}
		if (x.nextClean() != ']') {
			x.back();
			for (; ; ) {
				if (x.nextClean() == ',') {
					x.back();
					jsonArray.addRaw(JSONNull.NULL, filter);
				} else {
					x.back();
					jsonArray.addRaw(x.nextValue(), filter);
				}
				switch (x.nextClean()) {
					case ',':
						if (x.nextClean() == ']') {
							return;
						}
						x.back();
						break;
					case ']':
						return;
					default:
						throw x.syntaxError("Expected a ',' or ']'");
				}
			}
		}
	}
	
}

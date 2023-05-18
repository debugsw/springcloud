package com.spring.cloud.base.jwt.utils;

import com.spring.cloud.base.jwt.json.JSONArray;
import com.spring.cloud.base.jwt.json.JSONNull;
import com.spring.cloud.base.jwt.json.JSONString;
import com.spring.cloud.base.jwt.json.JSONTokener;
import com.spring.cloud.base.jwt.config.JSONConfig;
import com.spring.cloud.base.jwt.config.JSONObject;
import com.spring.cloud.base.jwt.exception.JSONException;
import com.spring.cloud.base.jwt.map.CaseInsensitiveLinkedMap;
import com.spring.cloud.base.jwt.map.CaseInsensitiveTreeMap;
import com.spring.cloud.base.utils.utils.ArrayUtil;
import com.spring.cloud.base.utils.CharUtil;
import com.spring.cloud.base.utils.Convert;
import com.spring.cloud.base.utils.bean.CopyOptions;
import com.spring.cloud.base.utils.crypto.NumberUtil;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.interf.Filter;
import com.spring.cloud.base.utils.str.StrUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: ls
 * @Description: 内部JSON工具类，仅用于JSON内部使用
 * @Date: 2023/4/25 11:29
 */
public final class InternalJSONUtil {

	private InternalJSONUtil() {
	}

	/**
	 * 如果对象是Number 且是 NaN or infinite，将抛出异常
	 *
	 * @param obj 被检查的对象
	 * @return 检测后的值
	 * @throws JSONException If o is a non-finite number.
	 */
	public	static Object testValidity(Object obj) throws JSONException {
		if (!ObjectUtil.isValidIfNumber(obj)) {
			throw new JSONException("JSON does not allow non-finite numbers.");
		}
		return obj;
	}

	/**
	 * 值转为String，用于JSON中
	 *
	 * @param value 需要转为字符串的对象
	 * @return 字符串
	 * @throws JSONException If the value is or contains an invalid number.
	 */
	public static String valueToString(Object value) throws JSONException {
		if (value == null || value instanceof JSONNull) {
			return JSONNull.NULL.toString();
		}
		if (value instanceof JSONString) {
			try {
				return ((JSONString) value).toJSONString();
			} catch (Exception e) {
				throw new JSONException(e);
			}
		} else if (value instanceof Number) {
			return NumberUtil.toStr((Number) value);
		} else if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
			return value.toString();
		} else if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;
			return new JSONObject(map).toString();
		} else if (value instanceof Collection) {
			Collection<?> coll = (Collection<?>) value;
			return new JSONArray(coll).toString();
		} else if (ArrayUtil.isArray(value)) {
			return new JSONArray(value).toString();
		} else {
			return JSONUtil.quote(value.toString());
		}
	}

	/**
	 * 尝试转换字符串为number, boolean, or null，无法转换返回String
	 *
	 * @param string A String.
	 * @return A simple JSON value.
	 */
	public static Object stringToValue(String string) {
		if (StrUtil.isEmpty(string) || StrUtil.NULL.equalsIgnoreCase(string)) {
			return JSONNull.NULL;
		}
		if ("true".equalsIgnoreCase(string)) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(string)) {
			return Boolean.FALSE;
		}
		char b = string.charAt(0);
		if ((b >= '0' && b <= '9') || b == '-') {
			try {
				if (StrUtil.containsAnyIgnoreCase(string, ".", "e")) {
					return new BigDecimal(string);
				} else {
					final long myLong = Long.parseLong(string);
					if (string.equals(Long.toString(myLong))) {
						if (myLong == (int) myLong) {
							return (int) myLong;
						} else {
							return myLong;
						}
					}
				}
			} catch (Exception ignore) {
			}
		}
		return string;
	}

	/**
	 * 将Property的键转化为JSON形式
	 *
	 * @param jsonObject JSONObject
	 * @param key        键
	 * @param value      值
	 * @return JSONObject
	 */
	public static JSONObject propertyPut(JSONObject jsonObject, Object key, Object value, Filter<MutablePair<String, Object>> filter) {
		final String[] path = StrUtil.splitToArray(Convert.toStr(key), CharUtil.DOT);
		final int last = path.length - 1;
		JSONObject target = jsonObject;
		for (int i = 0; i < last; i += 1) {
			final String segment = path[i];
			JSONObject nextTarget = target.getJSONObject(segment);
			if (nextTarget == null) {
				nextTarget = new JSONObject(target.getConfig());
				target.set(segment, nextTarget, filter, target.getConfig().isCheckDuplicate());
			}
			target = nextTarget;
		}
		target.set(path[last], value, filter, target.getConfig().isCheckDuplicate());
		return jsonObject;
	}

	/**
	 * 默认情况下是否忽略null值的策略选择，以下对象不忽略null值，其它对象忽略
	 *
	 * @param obj 需要检查的对象
	 * @return 是否忽略null值
	 */
	public static boolean defaultIgnoreNullValue(Object obj) {
		return (!(obj instanceof CharSequence))
				&& (!(obj instanceof JSONTokener))
				&& (!(obj instanceof Map));
	}

	/**
	 * 将{@link JSONConfig}参数转换为Bean拷贝所用的{@link CopyOptions}
	 *
	 * @param config {@link JSONConfig}
	 * @return {@link CopyOptions}
	 */
	public static CopyOptions toCopyOptions(JSONConfig config) {
		return CopyOptions.create()
				.setIgnoreCase(config.isIgnoreCase())
				.setIgnoreError(config.isIgnoreError())
				.setIgnoreNullValue(config.isIgnoreNullValue())
				.setTransientSupport(config.isTransientSupport());
	}

	/**
	 * 根据配置创建对应的原始Map
	 *
	 * @param capacity 初始大小
	 * @param config   JSON配置项，{@code null}则使用默认配置
	 * @return Map
	 */
	public static Map<String, Object> createRawMap(int capacity, JSONConfig config) {
		final Map<String, Object> rawHashMap;
		if (null == config) {
			config = JSONConfig.create();
		}
		final Comparator<String> keyComparator = config.getKeyComparator();
		if (config.isIgnoreCase()) {
			if (null != keyComparator) {
				rawHashMap = new CaseInsensitiveTreeMap<>(keyComparator);
			} else {
				rawHashMap = new CaseInsensitiveLinkedMap<>(capacity);
			}
		} else {
			if (null != keyComparator) {
				rawHashMap = new TreeMap<>(keyComparator);
			} else {
				rawHashMap = new LinkedHashMap<>(capacity);
			}
		}
		return rawHashMap;
	}
}

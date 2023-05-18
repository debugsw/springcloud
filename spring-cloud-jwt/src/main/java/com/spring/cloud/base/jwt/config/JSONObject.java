package com.spring.cloud.base.jwt.config;

import com.spring.cloud.base.jwt.json.JSON;
import com.spring.cloud.base.jwt.json.JSONArray;
import com.spring.cloud.base.jwt.json.JSONConverter;
import com.spring.cloud.base.jwt.json.JSONGetter;
import com.spring.cloud.base.jwt.exception.JSONException;
import com.spring.cloud.base.jwt.utils.InternalJSONUtil;
import com.spring.cloud.base.jwt.utils.JSONUtil;
import com.spring.cloud.base.jwt.utils.MutablePair;
import com.spring.cloud.base.utils.utils.ArrayUtil;
import com.spring.cloud.base.utils.CollUtil;
import com.spring.cloud.base.utils.base.BeanPath;
import com.spring.cloud.base.utils.base.ReflectUtil;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.interf.Filter;
import com.spring.cloud.base.utils.map.CaseInsensitiveMap;
import com.spring.cloud.base.utils.map.MapUtil;
import com.spring.cloud.base.utils.map.MapWrapper;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

/**
 * @Author: ls
 * @Description: JSON对象
 * @Date: 2023/4/25 13:40
 */
public class JSONObject extends MapWrapper<String, Object> implements JSON, JSONGetter<String> {

	private static final long serialVersionUID = -330220388580734346L;

	/**
	 * 默认初始大小
	 */
	public static final int DEFAULT_CAPACITY = MapUtil.DEFAULT_INITIAL_CAPACITY;

	/**
	 * 配置项
	 */
	private JSONConfig config;

	/**
	 * 构造，初始容量为 {@link #DEFAULT_CAPACITY}，KEY无序
	 */
	public JSONObject() {
		this(DEFAULT_CAPACITY, false);
	}

	/**
	 * 构造，初始容量为 {@link #DEFAULT_CAPACITY}
	 *
	 * @param isOrder 是否有序
	 */
	public JSONObject(boolean isOrder) {
		this(DEFAULT_CAPACITY, isOrder);
	}

	/**
	 * 构造
	 *
	 * @param capacity 初始大小
	 * @param isOrder  是否有序
	 */
	public JSONObject(int capacity, boolean isOrder) {
		this(capacity, false, isOrder);
	}

	/**
	 * 构造
	 *
	 * @param capacity     初始大小
	 * @param isIgnoreCase 是否忽略KEY大小写
	 * @param isOrder      是否有序
	 * @deprecated isOrder无效
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public JSONObject(int capacity, boolean isIgnoreCase, boolean isOrder) {
		this(capacity, JSONConfig.create().setIgnoreCase(isIgnoreCase));
	}

	/**
	 * 构造
	 *
	 * @param config JSON配置项
	 */
	public JSONObject(JSONConfig config) {
		this(DEFAULT_CAPACITY, config);
	}

	/**
	 * 构造
	 *
	 * @param capacity 初始大小
	 * @param config   JSON配置项，{@code null}则使用默认配置
	 */
	public JSONObject(int capacity, JSONConfig config) {
		super(InternalJSONUtil.createRawMap(capacity, ObjectUtil.defaultIfNull(config, JSONConfig.create())));
		this.config = ObjectUtil.defaultIfNull(config, JSONConfig.create());
	}

	/**
	 * 构建JSONObject，JavaBean默认忽略null值，其它对象不忽略
	 *
	 * @param source JavaBean或者Map对象或者String
	 */
	public JSONObject(Object source) {
		this(source, InternalJSONUtil.defaultIgnoreNullValue(source));
	}

	/**
	 * 构建JSONObject
	 *
	 * @param source          JavaBean或者Map对象或者String
	 * @param ignoreNullValue 是否忽略空值
	 */
	public JSONObject(Object source, boolean ignoreNullValue) {
		this(source, JSONConfig.create().setIgnoreNullValue(ignoreNullValue));
	}

	/**
	 * 构建JSONObject
	 *
	 * @param source          JavaBean或者Map对象或者String
	 * @param ignoreNullValue 是否忽略空值，如果source为JSON字符串，不忽略空值
	 * @param isOrder         是否有序
	 * @deprecated isOrder参数不再需要，JSONObject默认有序！
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public JSONObject(Object source, boolean ignoreNullValue, boolean isOrder) {
		this(source, JSONConfig.create()
				.setIgnoreCase((source instanceof CaseInsensitiveMap))
				.setIgnoreNullValue(ignoreNullValue)
		);
	}

	/**
	 * 构建JSONObject
	 *
	 * @param source JavaBean或者Map对象或者String
	 * @param config JSON配置文件，{@code null}则使用默认配置
	 */
	public JSONObject(Object source, JSONConfig config) {
		this(source, config, null);
	}

	/**
	 * 构建JSONObject
	 *
	 * @param source JavaBean或者Map对象或者String
	 * @param config JSON配置文件，{@code null}则使用默认配置
	 * @param filter 键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤
	 */
	public JSONObject(Object source, JSONConfig config, Filter<MutablePair<String, Object>> filter) {
		this(DEFAULT_CAPACITY, config);
		ObjectMapper.of(source).map(this, filter);
	}

	/**
	 * 构建指定name列表对应的键值对为新的JSONObject
	 *
	 * @param source 包含需要字段的Bean对象或者Map对象
	 * @param names  需要构建JSONObject的字段名列表
	 */
	public JSONObject(Object source, String... names) {
		this();
		if (ArrayUtil.isEmpty(names)) {
			ObjectMapper.of(source).map(this, null);
			return;
		}
		if (source instanceof Map) {
			Object value;
			for (String name : names) {
				value = ((Map<?, ?>) source).get(name);
				this.set(name, value, null, getConfig().isCheckDuplicate());
			}
		} else {
			for (String name : names) {
				try {
					this.putOpt(name, ReflectUtil.getFieldValue(source, name));
				} catch (Exception ignore) {

				}
			}
		}
	}

	/**
	 * 从JSON字符串解析为JSON对象，对于排序单独配置参数
	 *
	 * @param source  以大括号 {} 包围的字符串，其中KEY和VALUE使用 : 分隔，每个键值对使用逗号分隔
	 * @param isOrder 是否有序
	 * @throws JSONException JSON字符串语法错误
	 * @deprecated isOrder无效
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public JSONObject(CharSequence source, boolean isOrder) throws JSONException {
		this(source, JSONConfig.create());
	}

	@Override
	public JSONConfig getConfig() {
		return this.config;
	}

	/**
	 * 设置转为字符串时的日期格式，默认为时间戳（null值）<br>
	 * 此方法设置的日期格式仅对转换为JSON字符串有效，对解析JSON为bean无效。
	 *
	 * @param format 格式，null表示使用时间戳
	 * @return this
	 */
	public JSONObject setDateFormat(String format) {
		this.config.setDateFormat(format);
		return this;
	}

	/**
	 * 将指定KEY列表的值组成新的JSONArray
	 *
	 * @param names KEY列表
	 * @return A JSONArray of values.
	 * @throws JSONException If any of the values are non-finite numbers.
	 */
	public JSONArray toJSONArray(Collection<String> names) throws JSONException {
		if (CollUtil.isEmpty(names)) {
			return null;
		}
		final JSONArray ja = new JSONArray(this.config);
		Object value;
		for (String name : names) {
			value = this.get(name);
			if (null != value) {
				ja.set(value);
			}
		}
		return ja;
	}

	@Override
	public Object getObj(String key, Object defaultValue) {
		return this.getOrDefault(key, defaultValue);
	}

	@Override
	public Object getByPath(String expression) {
		return BeanPath.create(expression).get(this);
	}

	@Override
	public <T> T getByPath(String expression, Class<T> resultType) {
		return JSONConverter.jsonConvert(resultType, getByPath(expression), getConfig());
	}

	@Override
	public void putByPath(String expression, Object value) {
		BeanPath.create(expression).set(this, value);
	}

	@Override
	@Deprecated
	public JSONObject put(String key, Object value) throws JSONException {
		return set(key, value);
	}

	public JSONObject set(String key, Object value) throws JSONException {
		return set(key, value, null, false);
	}

	public JSONObject set(String key, Object value, Filter<MutablePair<String, Object>> filter, boolean checkDuplicate) throws JSONException {
		if (null == key) {
			return this;
		}
		if (null != filter) {
			final MutablePair<String, Object> pair = new MutablePair<>(key, value);
			if (filter.accept(pair)) {
				key = pair.getKey();
				value = pair.getValue();
			} else {
				return this;
			}
		}
		final boolean ignoreNullValue = this.config.isIgnoreNullValue();
		if (ObjectUtil.isNull(value) && ignoreNullValue) {
			this.remove(key);
		} else {
			if (checkDuplicate && containsKey(key)) {
				throw new JSONException("Duplicate key \"{}\"", key);
			}
			super.put(key, JSONUtil.wrap(InternalJSONUtil.testValidity(value), this.config));
		}
		return this;
	}

	public JSONObject putOnce(String key, Object value) throws JSONException {
		return setOnce(key, value, null);
	}

	public JSONObject setOnce(String key, Object value, Filter<MutablePair<String, Object>> filter) throws JSONException {
		return set(key, value, filter, true);
	}

	public JSONObject putOpt(String key, Object value) throws JSONException {
		if (key != null && value != null) {
			this.set(key, value);
		}
		return this;
	}

	@Override
	public void putAll(Map<? extends String, ?> m) {
		for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
			this.set(entry.getKey(), entry.getValue());
		}
	}

	public JSONObject accumulate(String key, Object value) throws JSONException {
		InternalJSONUtil.testValidity(value);
		Object object = this.getObj(key);
		if (object == null) {
			this.set(key, value);
		} else if (object instanceof JSONArray) {
			((JSONArray) object).set(value);
		} else {
			this.set(key, JSONUtil.createArray(this.config).set(object).set(value));
		}
		return this;
	}

	public JSONObject append(String key, Object value) throws JSONException {
		InternalJSONUtil.testValidity(value);
		Object object = this.getObj(key);
		if (object == null) {
			this.set(key, new JSONArray(this.config).set(value));
		} else if (object instanceof JSONArray) {
			this.set(key, ((JSONArray) object).set(value));
		} else {
			throw new JSONException("JSONObject [" + key + "] is not a JSONArray.");
		}
		return this;
	}

	public JSONObject increment(String key) throws JSONException {
		Object value = this.getObj(key);
		if (value == null) {
			this.set(key, 1);
		} else if (value instanceof BigInteger) {
			this.set(key, ((BigInteger) value).add(BigInteger.ONE));
		} else if (value instanceof BigDecimal) {
			this.set(key, ((BigDecimal) value).add(BigDecimal.ONE));
		} else if (value instanceof Integer) {
			this.set(key, (Integer) value + 1);
		} else if (value instanceof Long) {
			this.set(key, (Long) value + 1);
		} else if (value instanceof Double) {
			this.set(key, (Double) value + 1);
		} else if (value instanceof Float) {
			this.set(key, (Float) value + 1);
		} else {
			throw new JSONException("Unable to increment [" + JSONUtil.quote(key) + "].");
		}
		return this;
	}

	/**
	 * 返回JSON字符串<br>
	 * 如果解析错误，返回{@code null}
	 *
	 * @return JSON字符串
	 */
	@Override
	public String toString() {
		return this.toJSONString(0);
	}

	/**
	 * 返回JSON字符串<br>
	 * 支持过滤器，即选择哪些字段或值不写出
	 *
	 * @param indentFactor 每层缩进空格数
	 * @param filter       过滤器，同时可以修改编辑键和值
	 * @return JSON字符串
	 */
	public String toJSONString(int indentFactor, Filter<MutablePair<Object, Object>> filter) {
		final StringWriter sw = new StringWriter();
		synchronized (sw.getBuffer()) {
			return this.write(sw, indentFactor, 0, filter).toString();
		}
	}

	@Override
	public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
		return write(writer, indentFactor, indent, null);
	}

	/**
	 * 将JSON内容写入Writer<br>
	 * 支持过滤器，即选择哪些字段或值不写出
	 *
	 * @param writer       writer
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @param filter       过滤器，同时可以修改编辑键和值
	 * @return Writer
	 * @throws JSONException JSON相关异常
	 */
	public Writer write(Writer writer, int indentFactor, int indent, Filter<MutablePair<Object, Object>> filter) throws JSONException {
		final JSONWriter jsonWriter = JSONWriter.of(writer, indentFactor, indent, config)
				.beginObj();
		this.forEach((key, value) -> jsonWriter.writeField(new MutablePair<>(key, value), filter));
		jsonWriter.end();

		return writer;
	}

	@Override
	public JSONObject clone() throws CloneNotSupportedException {
		final JSONObject clone = (JSONObject) super.clone();
		clone.config = this.config;
		return clone;
	}
}

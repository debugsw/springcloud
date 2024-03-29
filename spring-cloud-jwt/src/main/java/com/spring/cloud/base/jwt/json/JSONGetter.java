package com.spring.cloud.base.jwt.json;

import com.spring.cloud.base.jwt.OptNullBasicTypeFromObjectGetter;
import com.spring.cloud.base.jwt.config.NumberWithFormat;
import com.spring.cloud.base.jwt.utils.JSONUtil;
import com.spring.cloud.base.utils.Convert;
import com.spring.cloud.base.utils.date.DateUtil;
import com.spring.cloud.base.utils.date.LocalDateTimeUtil;
import com.spring.cloud.base.utils.exception.ConvertException;
import com.spring.cloud.base.utils.str.StrUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: ls
 * @Description: 用于JSON的Getter类
 * @Date: 2023/4/25 11:29
 */
public interface JSONGetter<K> extends OptNullBasicTypeFromObjectGetter<K> {

	/**
	 * 获取JSON配置
	 *
	 * @return {@link JSONConfig}
	 */
	JSONConfig getConfig();

	/**
	 * key对应值是否为{@code null}或无此key
	 *
	 * @param key 键
	 * @return true 无此key或值为{@code null}或{@link JSONNull#NULL}返回{@code false}，其它返回{@code true}
	 */
	default boolean isNull(K key) {
		return JSONUtil.isNull(this.getObj(key));
	}

	/**
	 * 获取字符串类型值，并转义不可见字符，如'\n'换行符会被转义为字符串"\n"
	 *
	 * @param key 键
	 * @return 字符串类型值
	 */
	default String getStrEscaped(K key) {
		return getStrEscaped(key, null);
	}

	/**
	 * 获取字符串类型值，并转义不可见字符，如'\n'换行符会被转义为字符串"\n"
	 *
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 字符串类型值
	 */
	default String getStrEscaped(K key, String defaultValue) {
		return JSONUtil.escape(getStr(key, defaultValue));
	}

	/**
	 * 获得JSONArray对象<br>
	 * 如果值为其它类型对象，尝试转换为{@link JSONArray}返回，否则抛出异常
	 *
	 * @param key KEY
	 * @return JSONArray对象，如果值为{@code null}，返回{@code null}，非JSONArray类型，尝试转换，转换失败抛出异常
	 */
	default JSONArray getJSONArray(K key) {
		final Object object = this.getObj(key);
		if (JSONUtil.isNull(object)) {
			return null;
		}
		if (object instanceof JSON) {
			return (JSONArray) object;
		}
		return new JSONArray(object, getConfig());
	}

	/**
	 * 获得JSONObject对象<br>
	 * 如果值为其它类型对象，尝试转换为{@link JSONObject}返回，否则抛出异常
	 *
	 * @param key KEY
	 * @return JSONObject对象，如果值为{@code null}，返回{@code null}，非JSONObject类型，尝试转换，转换失败抛出异常
	 */
	default JSONObject getJSONObject(K key) {
		final Object object = this.getObj(key);
		if (JSONUtil.isNull(object)) {
			return null;
		}
		if (object instanceof JSON) {
			return (JSONObject) object;
		}
		return new JSONObject(object, getConfig());
	}

	/**
	 * 从JSON中直接获取Bean对象<br>
	 * 先获取JSONObject对象，然后转为Bean对象
	 *
	 * @param <T>      Bean类型
	 * @param key      KEY
	 * @param beanType Bean类型
	 * @return Bean对象，如果值为null或者非JSONObject类型，返回null
	 */
	default <T> T getBean(K key, Class<T> beanType) {
		final JSONObject obj = getJSONObject(key);
		return (null == obj) ? null : obj.toBean(beanType);
	}

	/**
	 * 从JSON中直接获取Bean的List列表<br>
	 * 先获取JSONArray对象，然后转为Bean的List
	 *
	 * @param <T>      Bean类型
	 * @param key      KEY
	 * @param beanType Bean类型
	 * @return Bean的List，如果值为null或者非JSONObject类型，返回null
	 */
	default <T> List<T> getBeanList(K key, Class<T> beanType) {
		final JSONArray jsonArray = getJSONArray(key);
		return (null == jsonArray) ? null : jsonArray.toList(beanType);
	}

	@Override
	default Date getDate(K key, Date defaultValue) {
		
		final Object obj = getObj(key);
		if (JSONUtil.isNull(obj)) {
			return defaultValue;
		}
		if (obj instanceof Date) {
			return (Date) obj;
		} else if (obj instanceof NumberWithFormat) {
			return (Date) ((NumberWithFormat) obj).convert(Date.class, obj);
		}

		final Optional<String> formatOps = Optional.ofNullable(getConfig()).map(JSONConfig::getDateFormat);
		if (formatOps.isPresent()) {
			final String format = formatOps.get();
			if (StrUtil.isNotBlank(format)) {
				final String str = Convert.toStr(obj);
				if (null == str) {
					return defaultValue;
				}
				return DateUtil.parse(str, format);
			}
		}
		return Convert.toDate(obj, defaultValue);
	}

	/**
	 * 获取{@link LocalDateTime}类型值
	 *
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return {@link LocalDateTime}
	 */
	default LocalDateTime getLocalDateTime(K key, LocalDateTime defaultValue) {
		final Object obj = getObj(key);
		if (JSONUtil.isNull(obj)) {
			return defaultValue;
		}
		if (obj instanceof LocalDateTime) {
			return (LocalDateTime) obj;
		}
		final Optional<String> formatOps = Optional.ofNullable(getConfig()).map(JSONConfig::getDateFormat);
		if (formatOps.isPresent()) {
			final String format = formatOps.get();
			if (StrUtil.isNotBlank(format)) {
				final String str = Convert.toStr(obj);
				if (null == str) {
					return defaultValue;
				}
				return LocalDateTimeUtil.parse(str, format);
			}
		}
		return Convert.toLocalDateTime(obj, defaultValue);
	}

	/**
	 * 获取byte[]数据
	 *
	 * @param key 键
	 * @return 值
	 */
	default byte[] getBytes(K key) {
		return get(key, byte[].class);
	}

	/**
	 * 获取指定类型的对象<br>
	 * 转换失败或抛出异常
	 *
	 * @param <T>  获取的对象类型
	 * @param key  键
	 * @param type 获取对象类型
	 * @return 对象
	 * @throws ConvertException 转换异常
	 */
	default <T> T get(K key, Class<T> type) throws ConvertException {
		return get(key, type, false);
	}

	/**
	 * 获取指定类型的对象
	 *
	 * @param <T>         获取的对象类型
	 * @param key         键
	 * @param type        获取对象类型
	 * @param ignoreError 是否跳过转换失败的对象或值
	 * @return 对象
	 * @throws ConvertException 转换异常
	 */
	default <T> T get(K key, Class<T> type, boolean ignoreError) throws ConvertException {
		final Object value = this.getObj(key);
		if (JSONUtil.isNull(value)) {
			return null;
		}
		return JSONConverter.jsonConvert(type, value, JSONConfig.create().setIgnoreError(ignoreError));
	}
}

package com.spring.cloud.base.jwt.json;

import com.spring.cloud.base.jwt.common.BeanConverter;
import com.spring.cloud.base.jwt.config.GlobalSerializeMapping;
import com.spring.cloud.base.jwt.utils.InternalJSONUtil;
import com.spring.cloud.base.jwt.utils.JSONUtil;
import com.spring.cloud.base.utils.ArrayConverter;
import com.spring.cloud.base.utils.Convert;
import com.spring.cloud.base.utils.ConverterRegistry;
import com.spring.cloud.base.utils.utils.TypeUtil;
import com.spring.cloud.base.utils.base.Base64;
import com.spring.cloud.base.utils.base.ReflectUtil;
import com.spring.cloud.base.utils.bean.BeanUtil;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.exception.ConvertException;
import com.spring.cloud.base.utils.interf.Converter;
import com.spring.cloud.base.utils.str.StrUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @Author: ls
 * @Description: JSON转换器
 * @Date: 2023/4/25 11:29
 */
public class JSONConverter implements Converter<JSON> {

	static {

		final ConverterRegistry registry = ConverterRegistry.getInstance();
		registry.putCustom(JSON.class, JSONConverter.class);
		registry.putCustom(JSONObject.class, JSONConverter.class);
		registry.putCustom(JSONArray.class, JSONConverter.class);
	}

	/**
	 * JSONArray转数组
	 *
	 * @param jsonArray  JSONArray
	 * @param arrayClass 数组元素类型
	 * @return 数组对象
	 */
	protected static Object toArray(JSONArray jsonArray, Class<?> arrayClass) {
		return new ArrayConverter(arrayClass).convert(jsonArray, null);
	}

	/**
	 * 将JSONArray转换为指定类型的对量列表
	 *
	 * @param <T>         元素类型
	 * @param jsonArray   JSONArray
	 * @param elementType 对象元素类型
	 * @return 对象列表
	 */
	protected static <T> List<T> toList(JSONArray jsonArray, Class<T> elementType) {
		return Convert.toList(elementType, jsonArray);
	}

	/**
	 * JSON递归转换<br>
	 * 首先尝试JDK类型转换，如果失败尝试JSON转Bean<br>
	 * 如果遇到{@link JSONBeanParser}，则调用其{@link JSONBeanParser#parse(Object)}方法转换。
	 *
	 * @param <T>        转换后的对象类型
	 * @param targetType 目标类型
	 * @param value      值
	 * @param jsonConfig JSON配置
	 * @return 目标类型的值
	 * @throws ConvertException 转换失败
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonConvert(Type targetType, Object value, JSONConfig jsonConfig) throws ConvertException {
		if (JSONUtil.isNull(value)) {
			return null;
		}
		if (targetType instanceof Class) {
			final Class<?> clazz = (Class<?>) targetType;
			if (JSONBeanParser.class.isAssignableFrom(clazz)) {
				@SuppressWarnings("rawtypes") final JSONBeanParser target = (JSONBeanParser) ReflectUtil.newInstanceIfPossible(clazz);
				if (null == target) {
					throw new ConvertException("Can not instance [{}]", targetType);
				}
				target.parse(value);
				return (T) target;
			} else if (targetType == byte[].class && value instanceof CharSequence) {
				return (T) Base64.decode((CharSequence) value);
			}
		}
		return jsonToBean(targetType, value, jsonConfig.isIgnoreError());
	}

	/**
	 * JSON递归转换<br>
	 * 首先尝试JDK类型转换，如果失败尝试JSON转Bean
	 *
	 * @param <T>         转换后的对象类型
	 * @param targetType  目标类型
	 * @param value       值，JSON格式
	 * @param ignoreError 是否忽略转换错误
	 * @return 目标类型的值
	 * @throws ConvertException 转换失败
	 */
	protected static <T> T jsonToBean(Type targetType, Object value, boolean ignoreError) throws ConvertException {
		if (JSONUtil.isNull(value)) {
			return null;
		}
		if (value instanceof JSON) {
			final JSONDeserializer<?> deserializer = GlobalSerializeMapping.getDeserializer(targetType);
			if (null != deserializer) {
				return (T) deserializer.deserialize((JSON) value);
			}
			if (value instanceof JSONGetter
					&& targetType instanceof Class
					&& (!Map.Entry.class.isAssignableFrom((Class<?>) targetType)
					&& BeanUtil.hasSetter((Class<?>) targetType))) {
				final JSONConfig config = ((JSONGetter<?>) value).getConfig();
				final Converter<T> converter = new BeanConverter<>(targetType,
						InternalJSONUtil.toCopyOptions(config).setIgnoreError(ignoreError));
				return converter.convertWithCheck(value, null, ignoreError);
			}
		}
		final T targetValue = Convert.convertWithCheck(targetType, value, null, ignoreError);
		if (null == targetValue && !ignoreError) {
			if (StrUtil.isBlankIfStr(value)) {
				return null;
			}
			throw new ConvertException("Can not convert {} to type {}", value, ObjectUtil.defaultIfNull(TypeUtil.getClass(targetType), targetType));
		}
		return targetValue;
	}

	@Override
	public JSON convert(Object value, JSON defaultValue) throws IllegalArgumentException {
		return JSONUtil.parse(value);
	}
}

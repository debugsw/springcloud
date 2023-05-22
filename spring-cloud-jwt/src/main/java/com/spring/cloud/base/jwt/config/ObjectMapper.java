package com.spring.cloud.base.jwt.config;

import com.spring.cloud.base.jwt.json.JSONArray;
import com.spring.cloud.base.jwt.json.JSONParser;
import com.spring.cloud.base.jwt.json.JSONTokener;
import com.spring.cloud.base.jwt.common.XML;
import com.spring.cloud.base.jwt.exception.JSONException;
import com.spring.cloud.base.jwt.utils.InternalJSONUtil;
import com.spring.cloud.base.jwt.utils.JSONUtil;
import com.spring.cloud.base.jwt.utils.MutablePair;
import com.spring.cloud.base.utils.*;
import com.spring.cloud.base.utils.base.Mutable;
import com.spring.cloud.base.utils.bean.BeanUtil;
import com.spring.cloud.base.utils.interf.Filter;
import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.utils.ArrayUtil;
import com.spring.cloud.base.utils.utils.IoUtil;
import com.spring.cloud.base.utils.utils.TypeUtil;

import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @Author: ls
 * @Description: 对象和JSON映射器用于转换对象为JSON
 * @Date: 2023/4/25 13:36
 */
public class ObjectMapper {

	public static ObjectMapper of(Object source) {
		return new ObjectMapper(source);
	}

	private final Object source;

	public ObjectMapper(Object source) {
		this.source = source;
	}

	/**
	 * 将给定对象转换为{@link JSONObject}
	 *
	 * @param jsonObject 目标{@link JSONObject}
	 * @param filter     键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void map(JSONObject jsonObject, Filter<MutablePair<String, Object>> filter) {
		final Object source = this.source;
		if (null == source) {
			return;
		}
		final JSONSerializer serializer = GlobalSerializeMapping.getSerializer(source.getClass());
		if (serializer instanceof JSONObjectSerializer) {
			serializer.serialize(jsonObject, source);
			return;
		}
		if (source instanceof JSONArray) {
			throw new JSONException("Unsupported type [{}] to JSONObject!", source.getClass());
		}
		if (source instanceof Map) {
			for (final Map.Entry<?, ?> e : ((Map<?, ?>) source).entrySet()) {
				jsonObject.set(Convert.toStr(e.getKey()), e.getValue(), filter, jsonObject.getConfig().isCheckDuplicate());
			}
		} else if (source instanceof Map.Entry) {
			final Map.Entry entry = (Map.Entry) source;
			jsonObject.set(Convert.toStr(entry.getKey()), entry.getValue(), filter, jsonObject.getConfig().isCheckDuplicate());
		} else if (source instanceof CharSequence) {
			mapFromStr((CharSequence) source, jsonObject, filter);
		} else if (source instanceof Reader) {
			mapFromTokener(new JSONTokener((Reader) source, jsonObject.getConfig()), jsonObject, filter);
		} else if (source instanceof InputStream) {
			mapFromTokener(new JSONTokener((InputStream) source, jsonObject.getConfig()), jsonObject, filter);
		} else if (source instanceof byte[]) {
			mapFromTokener(new JSONTokener(IoUtil.toStream((byte[]) source), jsonObject.getConfig()), jsonObject, filter);
		} else if (source instanceof JSONTokener) {
			
			mapFromTokener((JSONTokener) source, jsonObject, filter);
		} else if (source instanceof ResourceBundle) {
			
			mapFromResourceBundle((ResourceBundle) source, jsonObject, filter);
		} else if (BeanUtil.isReadableBean(source.getClass())) {
			mapFromBean(source, jsonObject);
		}
	}

	/**
	 * 初始化
	 *
	 * @param jsonArray 目标{@link JSONArray}
	 * @param filter    键值对过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤
	 * @throws JSONException 非数组或集合
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void map(JSONArray jsonArray, Filter<Mutable<Object>> filter) throws JSONException {
		final Object source = this.source;
		if (null == source) {
			return;
		}
		final JSONSerializer serializer = GlobalSerializeMapping.getSerializer(source.getClass());
		if (null != serializer && JSONArray.class.equals(TypeUtil.getTypeArgument(serializer.getClass()))) {
			serializer.serialize(jsonArray, source);
		} else if (source instanceof CharSequence) {
			mapFromStr((CharSequence) source, jsonArray, filter);
		} else if (source instanceof Reader) {
			mapFromTokener(new JSONTokener((Reader) source, jsonArray.getConfig()), jsonArray, filter);
		} else if (source instanceof InputStream) {
			mapFromTokener(new JSONTokener((InputStream) source, jsonArray.getConfig()), jsonArray, filter);
		} else if (source instanceof byte[]) {
			final byte[] bytesSource = (byte[]) source;
			if (bytesSource.length > 1 && '[' == bytesSource[0] && ']' == bytesSource[bytesSource.length - 1]) {
				mapFromTokener(new JSONTokener(IoUtil.toStream(bytesSource), jsonArray.getConfig()), jsonArray, filter);
			} else {
				for (final byte b : bytesSource) {
					jsonArray.add(b);
				}
			}
		} else if (source instanceof JSONTokener) {
			mapFromTokener((JSONTokener) source, jsonArray, filter);
		} else {
			final Iterator<?> iter;
			if (ArrayUtil.isArray(source)) {
				iter = new ArrayIter<>(source);
			} else if (source instanceof Iterator<?>) {
				iter = ((Iterator<?>) source);
			} else if (source instanceof Iterable<?>) {
				iter = ((Iterable<?>) source).iterator();
			} else {
				if (!jsonArray.getConfig().isIgnoreError()) {
					throw new JSONException("JSONArray initial value should be a string or collection or array.");
				}
				return;
			}
			final JSONConfig config = jsonArray.getConfig();
			Object next;
			while (iter.hasNext()) {
				next = iter.next();
				
				if (next != source) {
					jsonArray.addRaw(JSONUtil.wrap(next, config), filter);
				}
			}
		}
	}

	/**
	 * 从{@link ResourceBundle}转换
	 *
	 * @param bundle     ResourceBundle
	 * @param jsonObject {@link JSONObject}
	 * @param filter     键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤
	 */
	private static void mapFromResourceBundle(ResourceBundle bundle, JSONObject jsonObject, Filter<MutablePair<String, Object>> filter) {
		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (key != null) {
				InternalJSONUtil.propertyPut(jsonObject, key, bundle.getString(key), filter);
			}
		}
	}

	/**
	 * 从字符串转换
	 *
	 * @param source     JSON字符串
	 * @param jsonObject {@link JSONObject}
	 * @param filter     键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤
	 */
	private static void mapFromStr(CharSequence source, JSONObject jsonObject, Filter<MutablePair<String, Object>> filter) {
		final String jsonStr = StrUtil.trim(source);
		if (StrUtil.startWith(jsonStr, '<')) {
			
			XML.toJSONObject(jsonObject, jsonStr, false);
			return;
		}
		mapFromTokener(new JSONTokener(StrUtil.trim(source), jsonObject.getConfig()), jsonObject, filter);
	}

	/**
	 * 初始化
	 *
	 * @param source    JSON字符串
	 * @param jsonArray {@link JSONArray}
	 * @param filter    值过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤
	 */
	private void mapFromStr(CharSequence source, JSONArray jsonArray, Filter<Mutable<Object>> filter) {
		if (null != source) {
			mapFromTokener(new JSONTokener(StrUtil.trim(source), jsonArray.getConfig()), jsonArray, filter);
		}
	}

	/**
	 * 从{@link JSONTokener}转换
	 *
	 * @param x          JSONTokener
	 * @param jsonObject {@link JSONObject}
	 * @param filter     键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作
	 */
	private static void mapFromTokener(JSONTokener x, JSONObject jsonObject, Filter<MutablePair<String, Object>> filter) {
		JSONParser.of(x).parseTo(jsonObject, filter);
	}

	/**
	 * 初始化
	 *
	 * @param x         {@link JSONTokener}
	 * @param jsonArray {@link JSONArray}
	 * @param filter    值过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤
	 */
	private static void mapFromTokener(JSONTokener x, JSONArray jsonArray, Filter<Mutable<Object>> filter) {
		JSONParser.of(x).parseTo(jsonArray, filter);
	}

	/**
	 * 从Bean转换
	 *
	 * @param bean       Bean对象
	 * @param jsonObject {@link JSONObject}
	 */
	private static void mapFromBean(Object bean, JSONObject jsonObject) {
		BeanUtil.beanToMap(bean, jsonObject, InternalJSONUtil.toCopyOptions(jsonObject.getConfig()));
	}
}

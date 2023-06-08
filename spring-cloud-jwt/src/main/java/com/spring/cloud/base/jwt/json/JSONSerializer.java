package com.spring.cloud.base.jwt.json;

import com.spring.cloud.base.jwt.json.JSON;

/**
 * @Author: ls
 * @Description: 序列化接口，通过实现此接口，实现自定义的对象转换为JSON的操作
 * @Date: 2023/4/25 13:36
 */
@FunctionalInterface
public interface JSONSerializer<T extends JSON, V> {

	/**
	 * 序列化实现，通过实现此方法，将指定类型的对象转换为JSON对象
	 *
	 * @param json JSON，可以为JSONObject或者JSONArray
	 * @param bean 指定类型对象
	 */
	void serialize(T json, V bean);
}

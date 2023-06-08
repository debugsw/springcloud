package com.spring.cloud.base.jwt.json;

import com.spring.cloud.base.jwt.json.JSON;

/**
 * @Author: ls
 * @Description: JSON反序列话自定义实现类
 * @Date: 2023/4/25 13:36
 */
@FunctionalInterface
public interface JSONDeserializer<T> {

	/**
	 * 反序列化，通过实现此方法，自定义实现JSON转换为指定类型的逻辑
	 *
	 * @param json {@link JSON}
	 * @return 目标对象
	 */
	T deserialize(JSON json);
}

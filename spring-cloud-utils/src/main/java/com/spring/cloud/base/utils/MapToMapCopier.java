package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.abstra.AbsCopier;
import com.spring.cloud.base.utils.bean.CopyOptions;
import com.spring.cloud.base.utils.utils.TypeUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @Author: ls
 * @Description: Map属性拷贝到Map中的拷贝器
 * @Date: 2023/4/13 16:11
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MapToMapCopier extends AbsCopier<Map, Map> {

	/**
	 * 目标的类型（用于泛型类注入）
	 */
	private final Type targetType;

	/**
	 * 构造
	 *
	 * @param source      来源Map
	 * @param target      目标Bean对象
	 * @param targetType  目标泛型类型
	 * @param copyOptions 拷贝选项
	 */
	public MapToMapCopier(Map source, Map target, Type targetType, CopyOptions copyOptions) {
		super(source, target, copyOptions);
		this.targetType = targetType;
	}

	@Override
	public Map copy() {
		this.source.forEach((sKey, sValue) -> {
			if (null == sKey) {
				return;
			}
			// 忽略空值
			if (true == copyOptions.ignoreNullValue && sValue == null) {
				return;
			}

			final String sKeyStr = copyOptions.editFieldName(sKey.toString());
			// 对key做转换，转换后为null的跳过
			if (null == sKeyStr) {
				return;
			}

			// 忽略不需要拷贝的 key,
			if (false == copyOptions.testKeyFilter(sKeyStr)) {
				return;
			}

			final Object targetValue = target.get(sKeyStr);
			// 非覆盖模式下，如果目标值存在，则跳过
			if (false == copyOptions.override && null != targetValue) {
				return;
			}
			final Type[] typeArguments = TypeUtil.getTypeArguments(this.targetType);
			if (null != typeArguments) {
				sValue = this.copyOptions.convertField(typeArguments[1], sValue);
				sValue = copyOptions.editFieldValue(sKeyStr, sValue);
			}
			target.put(sKeyStr, sValue);
		});
		return this.target;
	}
}

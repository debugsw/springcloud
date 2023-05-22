package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.interf.Converter;
import com.spring.cloud.base.utils.utils.CollUtil;
import com.spring.cloud.base.utils.utils.TypeUtil;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @Author: ls
 * @Description: 各种集合类转换器
 * @Date: 2023/4/13 16:11
 */
public class CollectionConverter implements Converter<Collection<?>> {

	/**
	 * 集合类型
	 */
	private final Type collectionType;
	/**
	 * 集合元素类型
	 */
	private final Type elementType;

	/**
	 * 构造，默认集合类型使用{@link Collection}
	 */
	public CollectionConverter() {
		this(Collection.class);
	}

	/**
	 * 构造
	 *
	 * @param collectionType 集合类型
	 */
	public CollectionConverter(Type collectionType) {
		this(collectionType, TypeUtil.getTypeArgument(collectionType));
	}

	/**
	 * 构造
	 *
	 * @param collectionType 集合类型
	 */
	public CollectionConverter(Class<?> collectionType) {
		this(collectionType, TypeUtil.getTypeArgument(collectionType));
	}

	/**
	 * 构造
	 *
	 * @param collectionType 集合类型
	 * @param elementType    集合元素类型
	 */
	public CollectionConverter(Type collectionType, Type elementType) {
		this.collectionType = collectionType;
		this.elementType = elementType;
	}
	// ---------------------------------------------------------------------------------------------- Constractor end

	@Override
	public Collection<?> convert(Object value, Collection<?> defaultValue) throws IllegalArgumentException {
		final Collection<?> result = convertInternal(value);
		return ObjectUtil.defaultIfNull(result, defaultValue);
	}

	/**
	 * 内部转换
	 *
	 * @param value 值
	 * @return 转换后的集合对象
	 */
	protected Collection<?> convertInternal(Object value) {
		final Collection<?> collection = CollUtil.create(TypeUtil.getClass(this.collectionType), TypeUtil.getClass(this.elementType));
		return CollUtil.addAll(collection, value, this.elementType);
	}
}

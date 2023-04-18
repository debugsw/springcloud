package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.crypto.ObjectUtil;

import java.util.Collection;
import java.util.Comparator;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/13 16:11
 */
public class CacheableSynthesizedAnnotationAttributeProcessor implements SynthesizedAnnotationAttributeProcessor {

	private final Table<String, Class<?>, Object> valueCaches = new RowKeyTable<>();
	private final Comparator<Hierarchical> annotationComparator;

	/**
	 * 创建一个带缓存的注解值选择器
	 *
	 * @param annotationComparator 注解比较器，排序更靠前的注解将被优先用于获取值
	 */
	public CacheableSynthesizedAnnotationAttributeProcessor(Comparator<Hierarchical> annotationComparator) {
		Assert.notNull(annotationComparator, "annotationComparator must not null");
		this.annotationComparator = annotationComparator;
	}

	/**
	 * 创建一个带缓存的注解值选择器，
	 * 默认按{@link SynthesizedAnnotation#getVerticalDistance()}和{@link SynthesizedAnnotation#getHorizontalDistance()}排序，
	 * 越靠前的越优先被取值。
	 */
	public CacheableSynthesizedAnnotationAttributeProcessor() {
		this(Hierarchical.DEFAULT_HIERARCHICAL_COMPARATOR);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttributeValue(String attributeName, Class<T> attributeType, Collection<? extends SynthesizedAnnotation> synthesizedAnnotations) {
		Object value = valueCaches.get(attributeName, attributeType);
		// 此处理论上不可能出现缓存值为nul的情况
		if (ObjectUtil.isNotNull(value)) {
			return (T)value;
		}
		value = synthesizedAnnotations.stream()
			.filter(ma -> ma.hasAttribute(attributeName, attributeType))
			.min(annotationComparator)
			.map(ma -> ma.getAttributeValue(attributeName))
			.orElse(null);
		valueCaches.put(attributeName, attributeType, value);
		return (T)value;
	}
}

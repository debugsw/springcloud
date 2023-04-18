package com.spring.cloud.base.utils.interf;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

/**
 * @Author: ls
 * @Description: 注解合成器
 * @Date: 2023/4/13 16:11
 */
public interface AnnotationSynthesizer {

	/**
	 * 获取合成注解来源最初来源
	 *
	 * @return 合成注解来源最初来源
	 */
	Object getSource();

	/**
	 * 合成注解选择器
	 *
	 * @return 注解选择器
	 */
	SynthesizedAnnotationSelector getAnnotationSelector();

	/**
	 * 获取合成注解后置处理器
	 *
	 * @return 合成注解后置处理器
	 */
	Collection<SynthesizedAnnotationPostProcessor> getAnnotationPostProcessors();

	/**
	 * 获取已合成的注解
	 *
	 * @param annotationType 注解类型
	 * @return 已合成的注解
	 */
	SynthesizedAnnotation getSynthesizedAnnotation(Class<?> annotationType);

	/**
	 * 获取全部的合成注解
	 *
	 * @return 合成注解
	 */
	Map<Class<? extends Annotation>, SynthesizedAnnotation> getAllSynthesizedAnnotation();

	/**
	 * 获取合成注解
	 *
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 类型
	 */
	<T extends Annotation> T synthesize(Class<T> annotationType);

}

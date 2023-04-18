package com.spring.cloud.base.utils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @Author: ls
 * @Description: 组合注解
 * @Date: 2023/4/13 16:11
 */
public class CombinationAnnotationElement implements AnnotatedElement, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建CombinationAnnotationElement
	 *
	 * @param element   需要解析注解的元素：可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param predicate 过滤器，{@link Predicate#test(Object)}返回{@code true}保留，否则不保留
	 * @return CombinationAnnotationElement
	 */
	public static CombinationAnnotationElement of(AnnotatedElement element, Predicate<Annotation> predicate) {
		return new CombinationAnnotationElement(element, predicate);
	}

	/**
	 * 注解类型与注解对象对应表
	 */
	private Map<Class<? extends Annotation>, Annotation> annotationMap;
	/**
	 * 直接注解类型与注解对象对应表
	 */
	private Map<Class<? extends Annotation>, Annotation> declaredAnnotationMap;
	/**
	 * 过滤器
	 */
	private final Predicate<Annotation> predicate;

	/**
	 * 构造
	 *
	 * @param element 需要解析注解的元素：可以是Class、Method、Field、Constructor、ReflectPermission
	 */
	public CombinationAnnotationElement(AnnotatedElement element) {
		this(element, null);
	}

	/**
	 * 构造
	 *
	 * @param element   需要解析注解的元素：可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param predicate 过滤器，{@link Predicate#test(Object)}返回{@code true}保留，否则不保留
	 */
	public CombinationAnnotationElement(AnnotatedElement element, Predicate<Annotation> predicate) {
		this.predicate = predicate;
		init(element);
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return annotationMap.containsKey(annotationClass);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		Annotation annotation = annotationMap.get(annotationClass);
		return (annotation == null) ? null : (T) annotation;
	}

	@Override
	public Annotation[] getAnnotations() {
		final Collection<Annotation> annotations = this.annotationMap.values();
		return annotations.toArray(new Annotation[0]);
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		final Collection<Annotation> annotations = this.declaredAnnotationMap.values();
		return annotations.toArray(new Annotation[0]);
	}

	/**
	 * 初始化
	 *
	 * @param element 元素
	 */
	private void init(AnnotatedElement element) {
		final Annotation[] declaredAnnotations = element.getDeclaredAnnotations();
		this.declaredAnnotationMap = new TableMap<>();
		parseDeclared(declaredAnnotations);

		final Annotation[] annotations = element.getAnnotations();
		if (Arrays.equals(declaredAnnotations, annotations)) {
			this.annotationMap = this.declaredAnnotationMap;
		} else {
			this.annotationMap = new TableMap<>();
			parse(annotations);
		}
	}

	/**
	 * 进行递归解析注解，直到全部都是元注解为止
	 *
	 * @param annotations Class, Method, Field等
	 */
	private void parseDeclared(Annotation[] annotations) {
		Class<? extends Annotation> annotationType;
		// 直接注解
		for (Annotation annotation : annotations) {
			annotationType = annotation.annotationType();
			// issue#I5FQGW@Gitee：跳过元注解和已经处理过的注解，防止递归调用
			if (AnnotationUtil.isNotJdkMateAnnotation(annotationType)
					&& false == declaredAnnotationMap.containsKey(annotationType)) {
				if (test(annotation)) {
					declaredAnnotationMap.put(annotationType, annotation);
				}
				// 测试不通过的注解，不影响继续递归
				parseDeclared(annotationType.getDeclaredAnnotations());
			}
		}
	}

	/**
	 * 进行递归解析注解，直到全部都是元注解为止
	 *
	 * @param annotations Class, Method, Field等
	 */
	private void parse(Annotation[] annotations) {
		Class<? extends Annotation> annotationType;
		for (Annotation annotation : annotations) {
			annotationType = annotation.annotationType();
			// issue#I5FQGW@Gitee：跳过元注解和已经处理过的注解，防止递归调用
			if (AnnotationUtil.isNotJdkMateAnnotation(annotationType)
					&& false == declaredAnnotationMap.containsKey(annotationType)) {
				if (test(annotation)) {
					annotationMap.put(annotationType, annotation);
				}
				// 测试不通过的注解，不影响继续递归
				parse(annotationType.getAnnotations());
			}
		}
	}

	/**
	 * 检查给定的注解是否符合过滤条件
	 *
	 * @param annotation 注解对象
	 * @return 是否符合条件
	 */
	private boolean test(Annotation annotation) {
		return null == this.predicate || this.predicate.test(annotation);
	}
}

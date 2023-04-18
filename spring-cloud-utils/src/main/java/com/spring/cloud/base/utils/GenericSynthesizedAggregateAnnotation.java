package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.abstra.AbstractAnnotationSynthesizer;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.map.Opt;

import java.lang.annotation.Annotation;
import java.util.*;

public class GenericSynthesizedAggregateAnnotation
		extends AbstractAnnotationSynthesizer<List<Annotation>>
		implements SynthesizedAggregateAnnotation {

	/**
	 * 根对象
	 */
	private final Object root;

	/**
	 * 距离根对象的垂直距离
	 */
	private final int verticalDistance;

	/**
	 * 距离根对象的水平距离
	 */
	private final int horizontalDistance;

	/**
	 * 合成注解属性处理器
	 */
	private final SynthesizedAnnotationAttributeProcessor attributeProcessor;

	/**
	 * 基于指定根注解，为其与其元注解的层级结构中的全部注解构造一个合成注解。
	 * 当层级结构中出现了相同的注解对象时，将优先选择以距离根注解最近，且优先被扫描的注解对象,
	 * 当获取值时，同样遵循该规则。
	 *
	 * @param source 源注解
	 */
	public GenericSynthesizedAggregateAnnotation(Annotation... source) {
		this(Arrays.asList(source), new MetaAnnotationScanner());
	}

	/**
	 * 基于指定根注解，为其层级结构中的全部注解构造一个合成注解。
	 * 若扫描器支持对注解的层级结构进行扫描，则若层级结构中出现了相同的注解对象时，
	 * 将优先选择以距离根注解最近，且优先被扫描的注解对象，并且当获取注解属性值时同样遵循该规则。
	 *
	 * @param source            源注解
	 * @param annotationScanner 注解扫描器，该扫描器必须支持扫描注解类
	 */
	public GenericSynthesizedAggregateAnnotation(List<Annotation> source, AnnotationScanner annotationScanner) {
		this(
				source, SynthesizedAnnotationSelector.NEAREST_AND_OLDEST_PRIORITY,
				new CacheableSynthesizedAnnotationAttributeProcessor(),
				Arrays.asList(
						SynthesizedAnnotationPostProcessor.ALIAS_ANNOTATION_POST_PROCESSOR,
						SynthesizedAnnotationPostProcessor.MIRROR_LINK_ANNOTATION_POST_PROCESSOR,
						SynthesizedAnnotationPostProcessor.ALIAS_LINK_ANNOTATION_POST_PROCESSOR
				),
				annotationScanner
		);
	}

	/**
	 * 基于指定根注解，为其层级结构中的全部注解构造一个合成注解
	 *
	 * @param source                   当前查找的注解对象
	 * @param annotationSelector       合成注解选择器
	 * @param attributeProcessor       注解属性处理器
	 * @param annotationPostProcessors 注解后置处理器
	 * @param annotationScanner        注解扫描器，该扫描器必须支持扫描注解类
	 */
	public GenericSynthesizedAggregateAnnotation(
			List<Annotation> source,
			SynthesizedAnnotationSelector annotationSelector,
			SynthesizedAnnotationAttributeProcessor attributeProcessor,
			Collection<SynthesizedAnnotationPostProcessor> annotationPostProcessors,
			AnnotationScanner annotationScanner) {
		this(
				null, 0, 0,
				source, annotationSelector, attributeProcessor, annotationPostProcessors, annotationScanner
		);
	}

	/**
	 * 基于指定根注解，为其层级结构中的全部注解构造一个合成注解
	 *
	 * @param root                     根对象
	 * @param verticalDistance         距离根对象的水平距离
	 * @param horizontalDistance       距离根对象的垂直距离
	 * @param source                   当前查找的注解对象
	 * @param annotationSelector       合成注解选择器
	 * @param attributeProcessor       注解属性处理器
	 * @param annotationPostProcessors 注解后置处理器
	 * @param annotationScanner        注解扫描器，该扫描器必须支持扫描注解类
	 */
	GenericSynthesizedAggregateAnnotation(
			Object root, int verticalDistance, int horizontalDistance,
			List<Annotation> source,
			SynthesizedAnnotationSelector annotationSelector,
			SynthesizedAnnotationAttributeProcessor attributeProcessor,
			Collection<SynthesizedAnnotationPostProcessor> annotationPostProcessors,
			AnnotationScanner annotationScanner) {
		super(source, annotationSelector, annotationPostProcessors, annotationScanner);
		Assert.notNull(attributeProcessor, "attributeProcessor must not null");

		this.root = ObjectUtil.defaultIfNull(root, this);
		this.verticalDistance = verticalDistance;
		this.horizontalDistance = horizontalDistance;
		this.attributeProcessor = attributeProcessor;
	}

	/**
	 * 获取根对象
	 *
	 * @return 根对象
	 */
	@Override
	public Object getRoot() {
		return root;
	}

	/**
	 * 获取与根对象的垂直距离
	 *
	 * @return 与根对象的垂直距离
	 */
	@Override
	public int getVerticalDistance() {
		return verticalDistance;
	}

	/**
	 * 获取与根对象的水平距离
	 *
	 * @return 获取与根对象的水平距离
	 */
	@Override
	public int getHorizontalDistance() {
		return horizontalDistance;
	}

	/**
	 * 按广度优先扫描{@link #source}上的元注解
	 */
	@Override
	protected Map<Class<? extends Annotation>, SynthesizedAnnotation> loadAnnotations() {
		Map<Class<? extends Annotation>, SynthesizedAnnotation> annotationMap = new LinkedHashMap<>();

		// 根注解默认水平坐标为0，根注解的元注解坐标从1开始
		for (int i = 0; i < source.size(); i++) {
			final Annotation sourceAnnotation = source.get(i);
			Assert.isFalse(AnnotationUtil.isSynthesizedAnnotation(sourceAnnotation), "source [{}] has been synthesized");
			annotationMap.put(sourceAnnotation.annotationType(), new MetaAnnotation(sourceAnnotation, sourceAnnotation, 0, i));
			Assert.isTrue(
					annotationScanner.support(sourceAnnotation.annotationType()),
					"annotation scanner [{}] cannot support scan [{}]",
					annotationScanner, sourceAnnotation.annotationType()
			);
			annotationScanner.scan(
					(index, annotation) -> {
						SynthesizedAnnotation oldAnnotation = annotationMap.get(annotation.annotationType());
						SynthesizedAnnotation newAnnotation = new MetaAnnotation(sourceAnnotation, annotation, index + 1, annotationMap.size());
						if (ObjectUtil.isNull(oldAnnotation)) {
							annotationMap.put(annotation.annotationType(), newAnnotation);
						} else {
							annotationMap.put(annotation.annotationType(), annotationSelector.choose(oldAnnotation, newAnnotation));
						}
					},
					sourceAnnotation.annotationType(), null
			);
		}
		return annotationMap;
	}

	/**
	 * 获取合成注解属性处理器
	 *
	 * @return 合成注解属性处理器
	 */
	@Override
	public SynthesizedAnnotationAttributeProcessor getAnnotationAttributeProcessor() {
		return this.attributeProcessor;
	}

	/**
	 * 根据指定的属性名与属性类型获取对应的属性值，若存在{@link Alias}则获取{@link Alias#value()}指定的别名属性的值
	 * <p>当不同层级的注解之间存在同名同类型属性时，将优先获取更接近根注解的属性
	 *
	 * @param attributeName 属性名
	 * @param attributeType 属性类型
	 * @return 属性
	 */
	@Override
	public Object getAttributeValue(String attributeName, Class<?> attributeType) {
		return attributeProcessor.getAttributeValue(attributeName, attributeType, synthesizedAnnotationMap.values());
	}

	/**
	 * 获取合成注解中包含的指定注解
	 *
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return Opt.ofNullable(annotationType)
				.map(synthesizedAnnotationMap::get)
				.map(SynthesizedAnnotation::getAnnotation)
				.map(annotationType::cast)
				.orElse(null);
	}

	/**
	 * 当前合成注解中是否存在指定元注解
	 *
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
		return synthesizedAnnotationMap.containsKey(annotationType);
	}

	/**
	 * 获取合成注解中包含的全部注解
	 *
	 * @return 注解对象
	 */
	@Override
	public Annotation[] getAnnotations() {
		return synthesizedAnnotationMap.values().stream()
				.map(SynthesizedAnnotation::getAnnotation)
				.toArray(Annotation[]::new);
	}

	/**
	 * 若合成注解在存在指定元注解，则使用动态代理生成一个对应的注解实例
	 *
	 * @param annotationType 注解类型
	 * @return 合成注解对象
	 * @see SynthesizedAnnotationProxy#create(Class, AnnotationAttributeValueProvider, SynthesizedAnnotation)
	 */
	@Override
	public <T extends Annotation> T synthesize(Class<T> annotationType, SynthesizedAnnotation annotation) {
		return SynthesizedAnnotationProxy.create(annotationType, this, annotation);
	}

	/**
	 * 注解包装类，表示{@link #source}以及{@link #source}所属层级结构中的全部关联注解对象
	 *
	 * @author huangchengxing
	 */
	public static class MetaAnnotation extends GenericSynthesizedAnnotation<Annotation, Annotation> {

		/**
		 * 创建一个合成注解
		 *
		 * @param root               根对象
		 * @param annotation         被合成的注解对象
		 * @param verticalDistance   距离根对象的水平距离
		 * @param horizontalDistance 距离根对象的垂直距离
		 */
		protected MetaAnnotation(Annotation root, Annotation annotation, int verticalDistance, int horizontalDistance) {
			super(root, annotation, verticalDistance, horizontalDistance);
		}

	}

}

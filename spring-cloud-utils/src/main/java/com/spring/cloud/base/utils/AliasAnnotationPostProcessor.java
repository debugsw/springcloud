package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.interf.AnnotationAttribute;
import com.spring.cloud.base.utils.interf.AnnotationSynthesizer;
import com.spring.cloud.base.utils.interf.SynthesizedAnnotation;
import com.spring.cloud.base.utils.interf.SynthesizedAnnotationPostProcessor;
import com.spring.cloud.base.utils.map.*;

import java.util.Map;

/**
 * @Author: ls
 * @Description: 用于处理注解对象中带有注解的属性
 * @Date: 2023/4/13 16:11
 */
public class AliasAnnotationPostProcessor implements SynthesizedAnnotationPostProcessor {

	@Override
	public int order() {
		return Integer.MIN_VALUE;
	}

	@Override
	public void process(SynthesizedAnnotation synthesizedAnnotation, AnnotationSynthesizer synthesizer) {
		final Map<String, AnnotationAttribute> attributeMap = synthesizedAnnotation.getAttributes();

		// 记录别名与属性的关系
		final ForestMap<String, AnnotationAttribute> attributeAliasMappings = new LinkedForestMap<>(false);
		attributeMap.forEach((attributeName, attribute) -> {
			final String alias = Opt.ofNullable(attribute.getAnnotation(Alias.class))
					.map(Alias::value)
					.orElse(null);
			if (ObjectUtil.isNull(alias)) {
				return;
			}
			final AnnotationAttribute aliasAttribute = attributeMap.get(alias);
			Assert.notNull(aliasAttribute, "no method for alias: [{}]", alias);
			attributeAliasMappings.putLinkedNodes(alias, aliasAttribute, attributeName, attribute);
		});

		// 处理别名
		attributeMap.forEach((attributeName, attribute) -> {
			final AnnotationAttribute resolvedAttribute = Opt.ofNullable(attributeName)
					.map(attributeAliasMappings::getRootNode)
					.map(TreeEntry::getValue)
					.orElse(attribute);
			Assert.isTrue(
					ObjectUtil.isNull(resolvedAttribute)
							|| ClassUtil.isAssignable(attribute.getAttributeType(), resolvedAttribute.getAttributeType()),
					"return type of the root alias method [{}] is inconsistent with the original [{}]",
					resolvedAttribute.getClass(), attribute.getAttributeType()
			);
			if (attribute != resolvedAttribute) {
				attributeMap.put(attributeName, new ForceAliasedAnnotationAttribute(attribute, resolvedAttribute));
			}
		});
		synthesizedAnnotation.setAttributes(attributeMap);
	}

}

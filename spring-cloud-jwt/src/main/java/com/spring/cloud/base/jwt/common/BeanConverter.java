package com.spring.cloud.base.jwt.common;

import com.spring.cloud.base.utils.bean.BeanCopier;
import com.spring.cloud.base.utils.bean.BeanUtil;
import com.spring.cloud.base.utils.bean.CopyOptions;
import com.spring.cloud.base.utils.utils.TypeUtil;
import com.spring.cloud.base.utils.abstra.AbstractConverter;
import com.spring.cloud.base.utils.base.ReflectUtil;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.exception.ConvertException;
import com.spring.cloud.base.utils.interf.ValueProvider;
import com.spring.cloud.base.utils.map.MapProxy;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @Author: ls
 * @Description: Bean转换器
 * @Date: 2023/4/25 13:36
 */
public class BeanConverter<T> extends AbstractConverter<T> {
	private static final long serialVersionUID = 1L;

	private final Type beanType;
	private final Class<T> beanClass;
	private final CopyOptions copyOptions;

	/**
	 * 构造，默认转换选项，注入失败的字段忽略
	 *
	 * @param beanType 转换成的目标Bean类型
	 */
	public BeanConverter(Type beanType) {
		this(beanType, CopyOptions.create().setIgnoreError(true));
	}

	/**
	 * 构造，默认转换选项，注入失败的字段忽略
	 *
	 * @param beanClass 转换成的目标Bean类
	 */
	public BeanConverter(Class<T> beanClass) {
		this(beanClass, CopyOptions.create().setIgnoreError(true));
	}

	/**
	 * 构造
	 *
	 * @param beanType    转换成的目标Bean类
	 * @param copyOptions Bean转换选项参数
	 */
	@SuppressWarnings("unchecked")
	public BeanConverter(Type beanType, CopyOptions copyOptions) {
		this.beanType = beanType;
		this.beanClass = (Class<T>) TypeUtil.getClass(beanType);
		this.copyOptions = copyOptions;
	}

	@Override
	protected T convertInternal(Object value) {
		if (value instanceof Map ||
				value instanceof ValueProvider ||
				BeanUtil.isBean(value.getClass())) {
			if (value instanceof Map && this.beanClass.isInterface()) {
				
				return MapProxy.create((Map<?, ?>) value).toProxyBean(this.beanClass);
			}

			
			return BeanCopier.create(value, ReflectUtil.newInstanceIfPossible(this.beanClass), this.beanType, this.copyOptions).copy();
		} else if (value instanceof byte[]) {
			
			return ObjectUtil.deserialize((byte[]) value);
		}

		throw new ConvertException("Unsupported source type: {}", value.getClass());
	}

	@Override
	public Class<T> getTargetType() {
		return this.beanClass;
	}
}

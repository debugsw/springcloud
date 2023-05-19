package com.spring.cloud.base.utils.base;

import com.spring.cloud.base.utils.utils.AnnotationUtil;
import com.spring.cloud.base.utils.Convert;
import com.spring.cloud.base.utils.utils.ModifierUtil;
import com.spring.cloud.base.utils.TypeUtil;
import com.spring.cloud.base.utils.exception.BeanException;
import com.spring.cloud.base.utils.map.ClassUtil;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @Author: ls
 * @Description: 属性描述
 * @Date: 2023/4/13 16:11
 */
public class PropDesc {

	/**
	 * 字段
	 */
	final Field field;
	/**
	 * Getter方法
	 */
	protected Method getter;
	/**
	 * Setter方法
	 */
	protected Method setter;

	/**
	 * 构造
	 *
	 * @param field  字段
	 * @param getter get方法
	 * @param setter set方法
	 */
	public PropDesc(Field field, Method getter, Method setter) {
		this.field = field;
		this.getter = ClassUtil.setAccessible(getter);
		this.setter = ClassUtil.setAccessible(setter);
	}

	/**
	 * 获取字段名，如果存在Alias注解，读取注解的值作为名称
	 *
	 * @return 字段名
	 */
	public String getFieldName() {
		return ReflectUtil.getFieldName(this.field);
	}

	/**
	 * 获取字段名称
	 *
	 * @return 字段名
	 */
	public String getRawFieldName() {
		return null == this.field ? null : this.field.getName();
	}

	/**
	 * 获取字段
	 *
	 * @return 字段
	 */
	public Field getField() {
		return this.field;
	}

	/**
	 * 获得字段类型
	 *
	 * @return 字段类型
	 */
	public Type getFieldType() {
		if (null != this.field) {
			return TypeUtil.getType(this.field);
		}
		return findPropType(getter, setter);
	}

	/**
	 * 获得字段类型
	 *
	 * @return 字段类型
	 */
	public Class<?> getFieldClass() {
		if (null != this.field) {
			return TypeUtil.getClass(this.field);
		}
		return findPropClass(getter, setter);
	}

	/**
	 * 获取Getter方法，可能为{@code null}
	 *
	 * @return Getter方法
	 */
	public Method getGetter() {
		return this.getter;
	}

	/**
	 * 获取Setter方法，可能为{@code null}
	 *
	 * @return {@link Method}Setter 方法对象
	 */
	public Method getSetter() {
		return this.setter;
	}

	/**
	 * 检查属性是否可读（即是否可以通过{@link #getValue(Object)}获取到值）
	 *
	 * @param checkTransient 是否检查Transient关键字或注解
	 * @return 是否可读
	 */
	public boolean isReadable(boolean checkTransient) {
		if (null == this.getter && !ModifierUtil.isPublic(this.field)) {
			return false;
		}

		if (checkTransient && isTransientForGet()) {
			return false;
		}

		return !isIgnoreGet();
	}

	/**
	 * 获取属性值
	 *
	 * @param bean Bean对象
	 * @return 字段值
	 */
	public Object getValue(Object bean) {
		if (null != this.getter) {
			return ReflectUtil.invoke(bean, this.getter);
		} else if (ModifierUtil.isPublic(this.field)) {
			return ReflectUtil.getFieldValue(bean, this.field);
		}

		return null;
	}

	/**
	 * 获取属性值，自动转换属性值类型
	 *
	 * @param bean        Bean对象
	 * @param targetType  返回属性值需要转换的类型，null表示不转换
	 * @param ignoreError 是否忽略错误，包括转换错误和注入错误
	 * @return this
	 */
	public Object getValue(Object bean, Type targetType, boolean ignoreError) {
		Object result = null;
		try {
			result = getValue(bean);
		} catch (Exception e) {
			if (!ignoreError) {
				throw new BeanException(e, "Get value of [{}] error!", getFieldName());
			}
		}

		if (null != result && null != targetType) {
			return Convert.convertWithCheck(targetType, result, null, ignoreError);
		}
		return result;
	}

	/**
	 * 检查属性是否可读（即是否可以通过{@link #getValue(Object)}获取到值）
	 *
	 * @param checkTransient 是否检查Transient关键字或注解
	 * @return 是否可读
	 */
	public boolean isWritable(boolean checkTransient) {
		// 检查是否有getter方法或是否为public修饰
		if (null == this.setter && !ModifierUtil.isPublic(this.field)) {
			return false;
		}

		// 检查transient关键字和@Transient注解
		if (checkTransient && isTransientForSet()) {
			return false;
		}

		// 检查@PropIgnore注解
		return !isIgnoreSet();
	}

	/**
	 * 设置Bean的字段值
	 *
	 * @param bean  Bean对象
	 * @param value 值，必须与字段值类型匹配
	 * @return this
	 */
	public PropDesc setValue(Object bean, Object value) {
		if (null != this.setter) {
			ReflectUtil.invoke(bean, this.setter, value);
		} else if (ModifierUtil.isPublic(this.field)) {
			ReflectUtil.setFieldValue(bean, this.field, value);
		}
		return this;
	}

	/**
	 * 设置属性值，可以自动转换字段类型为目标类型
	 *
	 * @param bean        Bean对象
	 * @param value       属性值，可以为任意类型
	 * @param ignoreNull  是否忽略{@code null}值，true表示忽略
	 * @param ignoreError 是否忽略错误，包括转换错误和注入错误
	 * @return this
	 */
	public PropDesc setValue(Object bean, Object value, boolean ignoreNull, boolean ignoreError) {
		return setValue(bean, value, ignoreNull, ignoreError, true);
	}

	/**
	 * 设置属性值，可以自动转换字段类型为目标类型
	 *
	 * @param bean        Bean对象
	 * @param value       属性值，可以为任意类型
	 * @param ignoreNull  是否忽略{@code null}值，true表示忽略
	 * @param ignoreError 是否忽略错误，包括转换错误和注入错误
	 * @param override    是否覆盖目标值，如果不覆盖，会先读取bean的值，{@code null}则写，否则忽略。如果覆盖，则不判断直接写
	 * @return this
	 */
	public PropDesc setValue(Object bean, Object value, boolean ignoreNull, boolean ignoreError, boolean override) {
		if (null == value && ignoreNull) {
			return this;
		}

		// issue#I4JQ1N@Gitee
		// 非覆盖模式下，如果目标值存在，则跳过
		if (!override && null != getValue(bean)) {
			return this;
		}

		// 当类型不匹配的时候，执行默认转换
		if (null != value) {
			final Class<?> propClass = getFieldClass();
			if (!propClass.isInstance(value)) {
				value = Convert.convertWithCheck(propClass, value, null, ignoreError);
			}
		}

		// 属性赋值
		if (null != value || !ignoreNull) {
			try {
				this.setValue(bean, value);
			} catch (Exception e) {
				if (!ignoreError) {
					throw new BeanException(e, "Set value of [{}] error!", getFieldName());
				}
			}
		}

		return this;
	}

	/**
	 * 通过Getter和Setter方法中找到属性类型
	 *
	 * @param getter Getter方法
	 * @param setter Setter方法
	 * @return {@link Type}
	 */
	private Type findPropType(Method getter, Method setter) {
		Type type = null;
		if (null != getter) {
			type = TypeUtil.getReturnType(getter);
		}
		if (null == type && null != setter) {
			type = TypeUtil.getParamType(setter, 0);
		}
		return type;
	}

	/**
	 * 通过Getter和Setter方法中找到属性类型
	 *
	 * @param getter Getter方法
	 * @param setter Setter方法
	 * @return {@link Type}
	 */
	private Class<?> findPropClass(Method getter, Method setter) {
		Class<?> type = null;
		if (null != getter) {
			type = TypeUtil.getReturnClass(getter);
		}
		if (null == type && null != setter) {
			type = TypeUtil.getFirstParamClass(setter);
		}
		return type;
	}

	/**
	 * 检查字段是否被忽略写，通过{@link PropIgnore} 注解完成
	 *
	 * @return 是否忽略写
	 */
	private boolean isIgnoreSet() {
		return AnnotationUtil.hasAnnotation(this.field, PropIgnore.class)
				|| AnnotationUtil.hasAnnotation(this.setter, PropIgnore.class);
	}

	/**
	 * 检查字段是否被忽略读，通过{@link PropIgnore} 注解完成
	 *
	 * @return 是否忽略读
	 */
	private boolean isIgnoreGet() {
		return AnnotationUtil.hasAnnotation(this.field, PropIgnore.class)
				|| AnnotationUtil.hasAnnotation(this.getter, PropIgnore.class);
	}

	/**
	 * 字段和Getter方法是否为Transient关键字修饰的
	 *
	 * @return 是否为Transient关键字修饰的
	 */
	private boolean isTransientForGet() {
		boolean isTransient = ModifierUtil.hasModifier(this.field, ModifierUtil.ModifierType.TRANSIENT);

		// 检查Getter方法
		if (!isTransient && null != this.getter) {
			isTransient = ModifierUtil.hasModifier(this.getter, ModifierUtil.ModifierType.TRANSIENT);

			// 检查注解
			if (!isTransient) {
				isTransient = AnnotationUtil.hasAnnotation(this.getter, Transient.class);
			}
		}

		return isTransient;
	}

	/**
	 * 字段和Getter方法是否为Transient关键字修饰的
	 *
	 * @return 是否为Transient关键字修饰的
	 */
	private boolean isTransientForSet() {
		boolean isTransient = ModifierUtil.hasModifier(this.field, ModifierUtil.ModifierType.TRANSIENT);

		// 检查Getter方法
		if (!isTransient && null != this.setter) {
			isTransient = ModifierUtil.hasModifier(this.setter, ModifierUtil.ModifierType.TRANSIENT);

			// 检查注解
			if (!isTransient) {
				isTransient = AnnotationUtil.hasAnnotation(this.setter, Transient.class);
			}
		}

		return isTransient;
	}
}

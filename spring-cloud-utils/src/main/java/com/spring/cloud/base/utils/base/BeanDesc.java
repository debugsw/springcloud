package com.spring.cloud.base.utils.base;

import com.spring.cloud.base.utils.Assert;
import com.spring.cloud.base.utils.utils.ModifierUtil;
import com.spring.cloud.base.utils.map.BooleanUtil;
import com.spring.cloud.base.utils.map.CaseInsensitiveMap;
import com.spring.cloud.base.utils.str.StrUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: ls
 * @Description: Bean信息描述做为BeanInfo替代方案
 * @Date: 2023/4/13 16:11
 */
public class BeanDesc implements Serializable {

	private static final long serialVersionUID = -7708393886812930621L;
	/**
	 * Bean类
	 */
	private final Class<?> beanClass;
	/**
	 * 属性Map
	 */
	private final Map<String, PropDesc> propMap = new LinkedHashMap<>();

	/**
	 * 构造
	 *
	 * @param beanClass Bean类
	 */
	public BeanDesc(Class<?> beanClass) {
		Assert.notNull(beanClass);
		this.beanClass = beanClass;
		init();
	}

	/**
	 * 获取Bean的全类名
	 *
	 * @return Bean的类名
	 */
	public String getName() {
		return this.beanClass.getName();
	}

	/**
	 * 获取Bean的简单类名
	 *
	 * @return Bean的类名
	 */
	public String getSimpleName() {
		return this.beanClass.getSimpleName();
	}

	/**
	 * 获取字段名-字段属性Map
	 *
	 * @param ignoreCase 是否忽略大小写，true为忽略，false不忽略
	 * @return 字段名-字段属性Map
	 */
	public Map<String, PropDesc> getPropMap(boolean ignoreCase) {
		return ignoreCase ? new CaseInsensitiveMap<>(1, this.propMap) : this.propMap;
	}

	/**
	 * 获取字段属性列表
	 *
	 * @return {@link PropDesc} 列表
	 */
	public Collection<PropDesc> getProps() {
		return this.propMap.values();
	}

	/**
	 * 获取属性，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return {@link PropDesc}
	 */
	public PropDesc getProp(String fieldName) {
		return this.propMap.get(fieldName);
	}

	/**
	 * 获得字段名对应的字段对象，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return 字段值
	 */
	public Field getField(String fieldName) {
		final PropDesc desc = this.propMap.get(fieldName);
		return null == desc ? null : desc.getField();
	}

	/**
	 * 获取Getter方法，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return Getter方法
	 */
	public Method getGetter(String fieldName) {
		final PropDesc desc = this.propMap.get(fieldName);
		return null == desc ? null : desc.getGetter();
	}

	/**
	 * 获取Setter方法，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return Setter方法
	 */
	public Method getSetter(String fieldName) {
		final PropDesc desc = this.propMap.get(fieldName);
		return null == desc ? null : desc.getSetter();
	}

	/**
	 * 初始化
	 *
	 * @return this
	 */
	private BeanDesc init() {
		final Method[] gettersAndSetters = ReflectUtil.getMethods(this.beanClass, ReflectUtil::isGetterOrSetterIgnoreCase);
		PropDesc prop;
		for (Field field : ReflectUtil.getFields(this.beanClass)) {
			if (!ModifierUtil.isStatic(field) && !ReflectUtil.isOuterClassField(field)) {
				prop = createProp(field, gettersAndSetters);
				this.propMap.putIfAbsent(prop.getFieldName(), prop);
			}
		}
		return this;
	}

	/**
	 * 根据字段创建属性描述
	 * 查找Getter和Setter方法时会：
	 *
	 * @param field   字段
	 * @param methods 类中所有的方法
	 * @return {@link PropDesc}
	 */
	private PropDesc createProp(Field field, Method[] methods) {
		final PropDesc prop = findProp(field, methods, false);
		// 忽略大小写重新匹配一次
		if (null == prop.getter || null == prop.setter) {
			final PropDesc propIgnoreCase = findProp(field, methods, true);
			if (null == prop.getter) {
				prop.getter = propIgnoreCase.getter;
			}
			if (null == prop.setter) {
				prop.setter = propIgnoreCase.setter;
			}
		}

		return prop;
	}

	/**
	 * 查找字段对应的Getter和Setter方法
	 *
	 * @param field            字段
	 * @param gettersOrSetters 类中所有的Getter或Setter方法
	 * @param ignoreCase       是否忽略大小写匹配
	 * @return PropDesc
	 */
	private PropDesc findProp(Field field, Method[] gettersOrSetters, boolean ignoreCase) {
		final String fieldName = field.getName();
		final Class<?> fieldType = field.getType();
		final boolean isBooleanField = BooleanUtil.isBoolean(fieldType);
		Method getter = null;
		Method setter = null;
		String methodName;
		for (Method method : gettersOrSetters) {
			methodName = method.getName();
			if (method.getParameterCount() == 0) {
				// 无参数，可能为Getter方法
				if (isMatchGetter(methodName, fieldName, isBooleanField, ignoreCase)) {
					// 方法名与字段名匹配，则为Getter方法
					getter = method;
				}
			} else if (isMatchSetter(methodName, fieldName, isBooleanField, ignoreCase)) {
				// setter方法的参数类型和字段类型必须一致，或参数类型是字段类型的子类
				if (fieldType.isAssignableFrom(method.getParameterTypes()[0])) {
					setter = method;
				}
			}
			if (null != getter && null != setter) {
				// 如果Getter和Setter方法都找到了，不再继续寻找
				break;
			}
		}

		return new PropDesc(field, getter, setter);
	}

	/**
	 * 方法是否为Getter方法
	 * 匹配规则如下（忽略大小写）：
	 *
	 * <pre>
	 * 字段名    -》 方法名
	 * isName  -》 isName
	 * isName  -》 isIsName
	 * isName  -》 getIsName
	 * name     -》 isName
	 * name     -》 getName
	 * </pre>
	 *
	 * @param methodName     方法名
	 * @param fieldName      字段名
	 * @param isBooleanField 是否为Boolean类型字段
	 * @param ignoreCase     匹配是否忽略大小写
	 * @return 是否匹配
	 */
	private boolean isMatchGetter(String methodName, String fieldName, boolean isBooleanField, boolean ignoreCase) {
		final String handledFieldName;
		if (ignoreCase) {
			methodName = methodName.toLowerCase();
			handledFieldName = fieldName.toLowerCase();
			fieldName = handledFieldName;
		} else {
			handledFieldName = StrUtil.upperFirst(fieldName);
		}
		if (isBooleanField) {
			if (fieldName.startsWith("is")) {
				// 字段已经是is开头
				if (methodName.equals(fieldName) || ("get" + handledFieldName).equals(methodName) || ("is" + handledFieldName).equals(methodName)) {
					return true;
				}
			} else if (("is" + handledFieldName).equals(methodName)) {
				return true;
			}
		}
		return ("get" + handledFieldName).equals(methodName);
	}

	/**
	 * 方法是否为Setter方法
	 * 匹配规则如下（忽略大小写）：
	 *
	 * <pre>
	 * 字段名    -》 方法名
	 * isName  -》 setName
	 * isName  -》 setIsName
	 * name     -》 setName
	 * </pre>
	 *
	 * @param methodName     方法名
	 * @param fieldName      字段名
	 * @param isBooleanField 是否为Boolean类型字段
	 * @param ignoreCase     匹配是否忽略大小写
	 * @return 是否匹配
	 */
	private boolean isMatchSetter(String methodName, String fieldName, boolean isBooleanField, boolean ignoreCase) {
		final String handledFieldName;
		if (ignoreCase) {
			methodName = methodName.toLowerCase();
			handledFieldName = fieldName.toLowerCase();
			fieldName = handledFieldName;
		} else {
			handledFieldName = StrUtil.upperFirst(fieldName);
		}
		if (!methodName.startsWith("set")) {
			return false;
		}
		if (isBooleanField && fieldName.startsWith("is")) {
			if (("set" + StrUtil.removePrefix(fieldName, "is")).equals(methodName) || ("set" + handledFieldName).equals(methodName)) {
				return true;
			}
		}
		return ("set" + handledFieldName).equals(methodName);
	}
}

package com.spring.cloud.base.jwt.json;

import com.spring.cloud.base.jwt.config.JSONConfig;
import com.spring.cloud.base.jwt.exception.JSONException;
import com.spring.cloud.base.utils.base.BeanPath;
import com.spring.cloud.base.utils.map.TypeReference;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * @Author: ls
 * @Description: JSON接口
 * @Date: 2023/4/25 11:29
 */
public interface JSON extends Cloneable, Serializable {

	/**
	 * 获取JSON配置
	 *
	 * @return {@link JSONConfig}
	 */
	JSONConfig getConfig();

	/**
	 * 通过表达式获取JSON中嵌套的对象
	 *
	 * @param expression 表达式
	 * @return 对象
	 * @see BeanPath#get(Object)
	 */
	Object getByPath(String expression);

	/**
	 * 设置表达式指定位置（或filed对应）的值
	 *
	 * @param expression 表达式
	 * @param value      值
	 */
	void putByPath(String expression, Object value);

	/**
	 * 通过表达式获取JSON中嵌套的对象
	 *
	 * @param <T>        返回值类型
	 * @param expression 表达式
	 * @param resultType 返回值类型
	 * @return 对象
	 * @see BeanPath#get(Object)
	 */
	<T> T getByPath(String expression, Class<T> resultType);

	/**
	 * 格式化打印JSON，缩进为4个空格
	 *
	 * @return 格式化后的JSON字符串
	 * @throws JSONException 包含非法数抛出此异常
	 *
	 */
	default String toStringPretty() throws JSONException {
		return this.toJSONString(4);
	}

	/**
	 * 格式化输出JSON字符串
	 *
	 * @param indentFactor 每层缩进空格数
	 * @return JSON字符串
	 * @throws JSONException 包含非法数抛出此异常
	 */
	default String toJSONString(int indentFactor) throws JSONException {
		final StringWriter sw = new StringWriter();
		synchronized (sw.getBuffer()) {
			return this.write(sw, indentFactor, 0).toString();
		}
	}

	/**
	 * 将JSON内容写入Writer，无缩进<br>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param writer Writer
	 * @return Writer
	 * @throws JSONException JSON相关异常
	 */
	default Writer write(Writer writer) throws JSONException {
		return this.write(writer, 0, 0);
	}

	/**
	 * 将JSON内容写入Writer<br>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param writer       writer
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @return Writer
	 * @throws JSONException JSON相关异常
	 */
	Writer write(Writer writer, int indentFactor, int indent) throws JSONException;

	/**
	 * 转为实体类对象，转换异常将被抛出
	 *
	 * @param <T>   Bean类型
	 * @param clazz 实体类
	 * @return 实体类对象
	 */
	default <T> T toBean(Class<T> clazz) {
		return toBean((Type) clazz);
	}

	/**
	 * 转为实体类对象，转换异常将被抛出
	 *
	 * @param <T>       Bean类型
	 * @param reference {@link TypeReference}类型参考子类，可以获取其泛型参数中的Type类型
	 * @return 实体类对象
	 *
	 */
	default <T> T toBean(TypeReference<T> reference) {
		return toBean(reference.getType());
	}

	/**
	 * 转为实体类对象
	 *
	 * @param <T>  Bean类型
	 * @param type {@link Type}
	 * @return 实体类对象
	 *
	 */
	default <T> T toBean(Type type) {
		return JSONConverter.jsonConvert(type, this, getConfig());
	}

	/**
	 * 转为实体类对象
	 *
	 * @param <T>         Bean类型
	 * @param type        {@link Type}
	 * @param ignoreError 是否忽略转换错误
	 * @return 实体类对象
	 *
	 * @deprecated 请使用 {@link #toBean(Type)}, ignoreError在JSONConfig中生效
	 */
	@Deprecated
	default <T> T toBean(Type type, boolean ignoreError) {
		return JSONConverter.jsonConvert(type, this, JSONConfig.create().setIgnoreError(ignoreError));
	}
}

package com.spring.cloud.base.utils.crypto;

import com.spring.cloud.base.utils.utils.CollUtil;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: ls
 * @Description: 带有类验证的对象流用于避免反序列化漏洞
 * @Date: 2023/4/13 15:56
 */
public class ValidateObjectInputStream extends ObjectInputStream {

	private Set<String> whiteClassSet;
	private Set<String> blackClassSet;

	/**
	 * 构造
	 *
	 * @param inputStream   流
	 * @param acceptClasses 白名单的类
	 * @throws IOException IO异常
	 */
	public ValidateObjectInputStream(InputStream inputStream, Class<?>... acceptClasses) throws IOException {
		super(inputStream);
		accept(acceptClasses);
	}

	/**
	 * 禁止反序列化的类，用于反序列化验证
	 *
	 * @param refuseClasses 禁止反序列化的类
	 */
	public void refuse(Class<?>... refuseClasses) {
		if (null == this.blackClassSet) {
			this.blackClassSet = new HashSet<>();
		}
		for (Class<?> acceptClass : refuseClasses) {
			this.blackClassSet.add(acceptClass.getName());
		}
	}

	/**
	 * 接受反序列化的类，用于反序列化验证
	 *
	 * @param acceptClasses 接受反序列化的类
	 */
	public void accept(Class<?>... acceptClasses) {
		if (null == this.whiteClassSet) {
			this.whiteClassSet = new HashSet<>();
		}
		for (Class<?> acceptClass : acceptClasses) {
			this.whiteClassSet.add(acceptClass.getName());
		}
	}

	/**
	 * 只允许反序列化SerialObject class
	 */
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		validateClassName(desc.getName());
		return super.resolveClass(desc);
	}

	/**
	 * 验证反序列化的类是否合法
	 *
	 * @param className 类名
	 * @throws InvalidClassException 非法类
	 */
	private void validateClassName(String className) throws InvalidClassException {
		// 黑名单
		if (CollUtil.isNotEmpty(this.blackClassSet)) {
			if (this.blackClassSet.contains(className)) {
				throw new InvalidClassException("Unauthorized deserialization attempt by black list", className);
			}
		}

		if (CollUtil.isEmpty(this.whiteClassSet)) {
			return;
		}
		if (className.startsWith("java.")) {
			return;
		}
		if (this.whiteClassSet.contains(className)) {
			return;
		}

		throw new InvalidClassException("Unauthorized deserialization attempt", className);
	}
}

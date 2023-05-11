package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description: JDK相关工具类
 * @Date: 2023/4/13 16:11
 */
public class JdkUtil {
	/**
	 * JDK版本
	 */
	public static final int JVM_VERSION;
	/**
	 * 是否JDK8
	 */
	public static final boolean IS_JDK8;
	/**
	 * 是否大于等于JDK17
	 */
	public static final boolean IS_AT_LEAST_JDK17;

	/**
	 * 是否Android环境
	 */
	public static final boolean IS_ANDROID;

	static {
		// JVM版本
		JVM_VERSION = _getJvmVersion();
		IS_JDK8 = 8 == JVM_VERSION;
		IS_AT_LEAST_JDK17 = JVM_VERSION >= 17;

		// JVM名称
		final String jvmName = _getJvmName();
		IS_ANDROID = "Dalvik".equals(jvmName);
	}

	/**
	 * 获取JVM名称
	 *
	 * @return JVM名称
	 */
	private static String _getJvmName() {
		return System.getProperty("java.vm.name");
	}

	/**
	 * 根据{@code java.specification.version}属性值，获取版本号
	 *
	 * @return 版本号
	 */
	private static int _getJvmVersion() {
		int jvmVersion = -1;

		try{
			String javaSpecVer = System.getProperty("java.specification.version");
			if (StrUtil.isNotBlank(javaSpecVer)) {
				if (javaSpecVer.startsWith("1.")) {
					javaSpecVer = javaSpecVer.substring(2);
				}
				if (javaSpecVer.indexOf('.') == -1) {
					jvmVersion = Integer.parseInt(javaSpecVer);
				}
			}
		} catch (Throwable ignore){
			// 默认JDK8
			jvmVersion = 8;
		}

		return jvmVersion;
	}
}

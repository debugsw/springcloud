package com.spring.cloud.base.utils.crypto;

import java.security.Provider;
/**
 * @Author: ls
 * @Description: 单例
 * @Date: 2023/4/13 15:56
 */
public enum GlobalBouncyCastleProvider {
	/**
	 * 单例
	 */
	INSTANCE;

	private Provider provider;
	private static boolean useBouncyCastle = true;

	GlobalBouncyCastleProvider() {
		try {
			this.provider = ProviderFactory.createBouncyCastleProvider();
		} catch (NoClassDefFoundError | NoSuchMethodError e) {
			// ignore
		}
	}

	/**
	 * 获取{@link Provider}
	 *
	 * @return {@link Provider}
	 */
	public Provider getProvider() {
		return useBouncyCastle ? this.provider : null;
	}

	/**
	 * 设置是否使用Bouncy Castle库<br>
	 * 如果设置为false，表示强制关闭Bouncy Castle而使用JDK
	 *
	 * @param isUseBouncyCastle 是否使用BouncyCastle库
	 * @since 4.5.2
	 */
	public static void setUseBouncyCastle(boolean isUseBouncyCastle) {
		useBouncyCastle = isUseBouncyCastle;
	}
}

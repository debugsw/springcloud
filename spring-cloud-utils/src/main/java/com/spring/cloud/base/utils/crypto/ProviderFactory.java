package com.spring.cloud.base.utils.crypto;

import java.security.Provider;

/**
 * @Author: ls
 * @Description: Provider对象生产工厂类
 * @Date: 2023/4/13 15:56
 */
public class ProviderFactory {

	/**
	 * 创建Bouncy Castle 提供者<br>
	 * 如果用户未引入bouncycastle库，则此方法抛出{@link NoClassDefFoundError} 异常
	 *
	 * @return {@link Provider}
	 */
	public static Provider createBouncyCastleProvider() {
		final org.bouncycastle.jce.provider.BouncyCastleProvider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
		// issue#2631@Github
		SecureUtil.addProvider(provider);
		return provider;
	}
}

package com.spring.cloud.base.captcha;

import com.spring.cloud.base.utils.crypto.RandomUtil;
import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description: 随机字符验证码生成器
 * @Date: 2023/4/17 15:00
 */
public class RandomGenerator extends AbstractGenerator {

	private static final long serialVersionUID = -7802758587765561876L;

	/**
	 * 构造，使用字母+数字做为基础
	 *
	 * @param count 生成验证码长度
	 */
	public RandomGenerator(int count) {
		super(count);
	}

	/**
	 * 构造
	 *
	 * @param baseStr 基础字符集合，用于随机获取字符串的字符集合
	 * @param length  生成验证码长度
	 */
	public RandomGenerator(String baseStr, int length) {
		super(baseStr, length);
	}

	@Override
	public String generate() {
		return RandomUtil.randomString(this.baseStr, this.length);
	}

	@Override
	public boolean verify(String code, String userInputCode) {
		if (StrUtil.isNotBlank(userInputCode)) {
			return StrUtil.equalsIgnoreCase(code, userInputCode);
		}
		return false;
	}
}

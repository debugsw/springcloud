package com.springcloud.base.setting;

import com.spring.cloud.base.utils.Assert;
import com.spring.cloud.base.utils.map.SafeConcurrentHashMap;
import com.spring.cloud.base.utils.str.StrUtil;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Author: ls
 * @Description: Profile可以让我们定义一系列的配置信息，然后指定其激活条件
 * @Date: 2023/5/6 10:54
 */
public class Profile implements Serializable {
	private static final long serialVersionUID = -4189955219454008744L;

	/**
	 * 默认环境
	 */
	public static final String DEFAULT_PROFILE = "default";

	/**
	 * 条件
	 */
	private String profile;
	/**
	 * 编码
	 */
	private Charset charset;
	/**
	 * 是否使用变量
	 */
	private boolean useVar;
	/**
	 * 配置文件缓存
	 */
	private final Map<String, Setting> settingMap = new SafeConcurrentHashMap<>();

	/**
	 * 默认构造，环境使用默认的：default，编码UTF-8，不使用变量
	 */
	public Profile() {
		this(DEFAULT_PROFILE);
	}

	/**
	 * 构造，编码UTF-8，不使用变量
	 *
	 * @param profile 环境
	 */
	public Profile(String profile) {
		this(profile, Setting.DEFAULT_CHARSET, false);
	}

	/**
	 * 构造
	 *
	 * @param profile 环境
	 * @param charset 编码
	 * @param useVar  是否使用变量
	 */
	public Profile(String profile, Charset charset, boolean useVar) {
		this.profile = profile;
		this.charset = charset;
		this.useVar = useVar;
	}

	/**
	 * 获取当前环境下的配置文件
	 *
	 * @param name 文件名，如果没有扩展名，默认为.setting
	 * @return 当前环境下配置文件
	 */
	public Setting getSetting(String name) {
		String nameForProfile = fixNameForProfile(name);
		Setting setting = settingMap.get(nameForProfile);
		if (null == setting) {
			setting = new Setting(nameForProfile, this.charset, this.useVar);
			settingMap.put(nameForProfile, setting);
		}
		return setting;
	}

	/**
	 * 设置环境
	 *
	 * @param profile 环境
	 * @return 自身
	 */
	public Profile setProfile(String profile) {
		this.profile = profile;
		return this;
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 * @return 自身
	 */
	public Profile setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 设置是否使用变量
	 *
	 * @param useVar 变量
	 * @return 自身
	 */
	public Profile setUseVar(boolean useVar) {
		this.useVar = useVar;
		return this;
	}

	/**
	 * 清空所有环境的配置文件
	 *
	 * @return 自身
	 */
	public Profile clear() {
		this.settingMap.clear();
		return this;
	}

	/**
	 * 修正文件名
	 *
	 * @param name 文件名
	 * @return 修正后的文件名
	 */
	private String fixNameForProfile(String name) {
		Assert.notBlank(name, "Setting name must be not blank !");
		final String actralProfile = StrUtil.nullToEmpty(this.profile);
		if (!name.contains(StrUtil.DOT)) {
			return StrUtil.format("{}/{}.setting", actralProfile, name);
		}
		return StrUtil.format("{}/{}", actralProfile, name);
	}
}

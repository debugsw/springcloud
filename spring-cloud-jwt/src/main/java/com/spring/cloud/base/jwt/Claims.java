package com.spring.cloud.base.jwt;

import com.spring.cloud.base.jwt.config.JSONConfig;
import com.spring.cloud.base.jwt.config.JSONObject;
import com.spring.cloud.base.jwt.utils.JSONUtil;
import com.spring.cloud.base.utils.Assert;
import com.spring.cloud.base.utils.base.Base64;
import com.spring.cloud.base.utils.date.GlobalCustomFormat;
import com.spring.cloud.base.utils.map.MapUtil;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Author: ls
 * @Description: Claims 认证，简单的JSONObject包装
 * @Date: 2023/4/25 11:29
 */
public class Claims implements Serializable {
	private static final long serialVersionUID = 1L;

	// 时间使用秒级时间戳表示
	private final JSONConfig CONFIG = JSONConfig.create().setDateFormat(GlobalCustomFormat.FORMAT_SECONDS);

	private JSONObject claimJSON;

	/**
	 * 增加Claims属性，如果属性值为{@code null}，则移除这个属性
	 *
	 * @param name  属性名
	 * @param value 属性值
	 */
	protected void setClaim(String name, Object value) {
		init();
		Assert.notNull(name, "Name must be not null!");
		if (value == null) {
			claimJSON.remove(name);
			return;
		}
		claimJSON.set(name, value);
	}

	/**
	 * 加入多个Claims属性
	 *
	 * @param headerClaims 多个Claims属性
	 */
	protected void putAll(Map<String, ?> headerClaims) {
		if (MapUtil.isNotEmpty(headerClaims)) {
			for (Map.Entry<String, ?> entry : headerClaims.entrySet()) {
				setClaim(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * 获取指定名称属性
	 *
	 * @param name 名称
	 * @return 属性
	 */
	public Object getClaim(String name) {
		init();
		return this.claimJSON.getObj(name);
	}

	/**
	 * 获取Claims的JSON字符串形式
	 *
	 * @return JSON字符串
	 */
	public JSONObject getClaimsJson() {
		init();
		return this.claimJSON;
	}

	/**
	 * 解析JWT JSON
	 *
	 * @param tokenPart JWT JSON
	 * @param charset   编码
	 */
	public void parse(String tokenPart, Charset charset) {
		this.claimJSON = JSONUtil.parseObj(Base64.decodeStr(tokenPart, charset), CONFIG);
	}

	@Override
	public String toString() {
		init();
		return this.claimJSON.toString();
	}

	private void init() {
		if (null == this.claimJSON) {
			this.claimJSON = new JSONObject(CONFIG);
		}
	}
}

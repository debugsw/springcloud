package com.spring.cloud.base.http;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/26 15:00
 */
public class UserPassAuthenticator extends Authenticator {

	private final String user;
	private final char[] pass;

	/**
	 * 构造
	 *
	 * @param user 用户名
	 * @param pass 密码
	 */
	public UserPassAuthenticator(String user, char[] pass) {
		this.user = user;
		this.pass = pass;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.user, this.pass);
	}

}

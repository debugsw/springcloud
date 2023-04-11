package com.springcloud.base.core.config.auth;


import com.springcloud.base.core.auth.JwtInfoModel;

/**
 * @Author: Yangshan
 * @Date: 2020/4/28
 * @Description: 全局上下文
 **/
public class GlobalContext {


    /**
     * 后台权限JWT
     */
    public static final ThreadLocal<JwtInfoModel> AUTHORIZE_JWT = new ThreadLocal<>();




}

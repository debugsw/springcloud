package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.str.StrUtil;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/26 15:00
 */
public class FormUrlEncodedBody extends BytesBody {

    /**
     * 创建 Http request body
     *
     * @param form    表单
     * @param charset 编码
     * @return FormUrlEncodedBody
     */
    public static FormUrlEncodedBody create(Map<String, Object> form, Charset charset) {
        return new FormUrlEncodedBody(form, charset);
    }

    /**
     * 构造
     *
     * @param form    表单
     * @param charset 编码
     */
    public FormUrlEncodedBody(Map<String, Object> form, Charset charset) {
        super(StrUtil.bytes(UrlQuery.of(form, true).build(charset), charset));
    }

}

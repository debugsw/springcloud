package com.springcloud.base.core.exception;

import com.springcloud.base.core.result.ResultCode;
import org.springframework.lang.Nullable;

/**
 * @Author: ls
 * @Description: 自定义异常抛出工具
 * @Date: 2023/4/10 09:54
 */
public class AssertUtils {
    /**
     * 断定字符串不为空
     *
     * @param s       条件
     * @param message 消息体
     */
    public static void hasText(@Nullable String s, String message) {
        AssertUtils.hasText(s, ResultCode.DEFAULT_FAIL, message);
    }

    /**
     * 断定字符串不为空
     *
     * @param s       条件
     * @param code    错误码
     * @param message 消息体
     */
    public static void hasText(@Nullable String s, String code, String message) {
        if (null == s || "".equals(s)) {
            throw DefaultException.customException(code, message);
        }
    }

    /**
     * 断定字符串不为空
     *
     * @param s       条件
     * @param message 消息体
     */
    public static void isBlank(@Nullable String s, String message) {
        AssertUtils.isBlank(s, ResultCode.DEFAULT_FAIL, message);
    }

    /**
     * 断定字符串不为空
     *
     * @param s       条件
     * @param code    错误码
     * @param message 消息体
     */
    public static void isBlank(@Nullable String s, String code, String message) {
        if (null != s && !"".equals(s)) {
            throw DefaultException.customException(code, message);
        }
    }

    /**
     * 断定不为空
     *
     * @param object  条件
     * @param message 消息体
     */
    public static void notNull(@Nullable Object object, String message) {
        AssertUtils.notNull(object, ResultCode.DEFAULT_FAIL, message);
    }

    /**
     * 断定不为空
     *
     * @param object  条件
     * @param code    错误码
     * @param message 消息体
     */
    public static void notNull(@Nullable Object object, String code, String message) {
        if (null == object) {
            throw DefaultException.customException(code, message);
        }
    }

    /**
     * 断定为空
     *
     * @param object  条件
     * @param message 消息体
     */
    public static void isEmpty(@Nullable Object object, String message) {
        AssertUtils.isEmpty(object, ResultCode.DEFAULT_FAIL, message);
    }

    /**
     * 断定为空
     *
     * @param object  条件
     * @param code    错误码
     * @param message 消息体
     */
    public static void isEmpty(@Nullable Object object, String code, String message) {
        if (null != object) {
            throw DefaultException.customException(code, message);
        }
    }

    /**
     * 断定不为空
     *
     * @param object  条件
     * @param message 消息体
     */
    public static void notEmpty(@Nullable Object object, String message) {
        AssertUtils.notEmpty(object, ResultCode.DEFAULT_FAIL, message);
    }

    /**
     * 断定不为空
     *
     * @param object  条件
     * @param code    错误码
     * @param message 消息体
     */
    public static void notEmpty(@Nullable Object object, String code, String message) {
        if (null == object) {
            throw DefaultException.customException(code, message);
        }
    }

    /**
     * 断定为真
     *
     * @param b       条件
     * @param message 消息体
     */
    public static void isTrue(Boolean b, String message) {
        AssertUtils.isTrue(b, ResultCode.DEFAULT_FAIL, message);
    }

    /**
     * 断定为真
     *
     * @param b       条件
     * @param code    错误码
     * @param message 消息体
     */
    public static void isTrue(Boolean b, String code, String message) {
        if (!b) {
            throw DefaultException.customException(code, message);
        }
    }

}

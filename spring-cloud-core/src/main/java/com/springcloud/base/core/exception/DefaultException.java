package com.springcloud.base.core.exception;

import com.springcloud.base.core.result.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: ls
 * @Description: 自定义默认异常
 * @Date: 2023/1/28 10:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultException extends RuntimeException {

    private static final long serialVersionUID = -3658390753852448234L;

    private String code;

    private String err;

    private Object data;

    public DefaultException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public DefaultException(String code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.data = data;
    }

    /**
     * 自定义错误
     *
     * @param code 错误编码
     * @param msg  提示语
     * @return
     */
    public static DefaultException customException(String code, String msg) {
        return new DefaultException(code, msg);
    }

    /**
     * 自定义错误
     *
     * @param code 错误编码
     * @param msg  提示语
     * @param data 数据
     * @return
     */
    public static DefaultException customException(String code, String msg, Object data) {
        return new DefaultException(code, msg, data);
    }

    /**
     * 业务默认错误
     *
     * @param msg 提示语
     * @return
     */
    public static DefaultException defaultException(String msg) {
        return new DefaultException(ResultCode.DEFAULT_FAIL, msg);
    }

    /**
     * 默认未知错误
     *
     * @return
     */
    public static DefaultException unknownException() {
        return new DefaultException(ResultCode.UNKNOWN_FAIL, "系统未知错误");
    }

    /**
     * 默认未知错误
     *
     * @return
     */
    public static DefaultException unknownException(String msg) {
        return new DefaultException(ResultCode.UNKNOWN_FAIL, msg);
    }
}

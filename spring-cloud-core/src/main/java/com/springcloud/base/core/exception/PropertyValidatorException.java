package com.springcloud.base.core.exception;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/10 09:54
 */
public class PropertyValidatorException extends DefaultException {

    private static final long serialVersionUID = 3052438913049213630L;

    public static final String CODE = "1022";

    public PropertyValidatorException(String msg) {
        super(CODE, msg);
    }
}

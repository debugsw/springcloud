package com.spring.cloud.base.monitor.exception;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/21 10:54
 */
public class TelnetException extends RuntimeException{

    private static final long serialVersionUID = 9115491416305319316L;

    public TelnetException(String message) {
        super( message );
    }

}

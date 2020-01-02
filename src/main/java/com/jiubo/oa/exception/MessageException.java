package com.jiubo.oa.exception;

/**
 * DesCribe：自定义异常
 */
public class MessageException extends Exception {

    private static final long serialVersionUID = 777251835374645766L;

    public MessageException(String message) {
        super(message);
    }

}

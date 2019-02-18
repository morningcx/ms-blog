package com.morningcx.ms.blog.base.exception;

/**
 * 异常类
 *
 * @author guochenxiao
 * @date 2019/2/18
 */
public class MsException extends RuntimeException{

    private MsException(String message) {
        super(message);
    }

    public static void occur(String message) {
        throw new MsException(message);
    }

}

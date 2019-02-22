package com.morningcx.ms.blog.base.exception;

/**
 * 异常类
 *
 * @author guochenxiao
 * @date 2019/2/18
 */
public class BaseException extends RuntimeException {

    private BaseException(String msg) {
        super(msg);
    }

    public static void throwBy(String msg) {
        throw new BaseException(msg);
    }

    public static void throwIf(boolean b, String msg) {
        if (b) {
            throwBy(msg);
        }
    }

    public static void throwIfNull(Object o, String msg) {
        throwIf(o == null || "".equals(o), msg);
    }

}

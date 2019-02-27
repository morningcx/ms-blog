package com.morningcx.ms.blog.base.exception;


/**
 * 异常类
 *
 * @author gcx
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

    /**
     * 判断对象是否为null
     *
     * @param o
     * @param msg
     */
    public static void throwIfNull(Object o, String msg) {
        throwIf(o == null, msg);
    }

    /**
     * 判断字符串是否为空，返回格式化后的字符串
     *
     * @param s
     * @param msg
     * @return
     */
    public static void throwIfEmpty(String s, String msg) {
        throwIf(s == null || "".equals(s.trim()), msg);
    }

}

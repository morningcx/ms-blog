package com.morningcx.ms.blog.base.exception;


/**
 * 异常类
 *
 * @author gcx
 * @date 2019/2/18
 */
public class BusinessException extends RuntimeException {

    private BusinessException(String msg) {
        super(msg);
    }

    public static void throwBy(String msg) {
        throw new BusinessException(msg);
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
    public static void throwNull(Object o, String msg) {
        throwIf(o == null, msg);
    }

    /**
     * 判断字符串是否为null或者为""
     *
     * @param s
     * @param msg
     * @return
     */
    public static void throwBlank(String s, String msg) {
        throwIf(s == null || "".equals(s.trim()), msg);
    }

}

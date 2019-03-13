package com.morningcx.ms.blog.base.exception;


/**
 * 异常类
 *
 * @author gcx
 * @date 2019/2/18
 */
public class BizException extends RuntimeException {

    private BizException(String msg) {
        super(msg);
    }

    public static void throwBy(String msg) {
        throw new BizException(msg);
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
     * 判断字符串是否为null或者为""
     *
     * @param s
     * @param msg
     * @return
     */
    public static void throwIfBlank(String s, String msg) {
        throwIf(s == null || "".equals(s.trim()), msg);
    }

}

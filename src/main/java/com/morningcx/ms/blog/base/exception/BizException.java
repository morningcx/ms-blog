package com.morningcx.ms.blog.base.exception;

/**
 * 异常类
 *
 * @author guochenxiao
 * @date 2019/2/18
 */
public class BizException extends RuntimeException {

    private BizException(String msg) {
        super(msg);
    }

    public static void cause(String msg) {
        throw new BizException(msg);
    }

}

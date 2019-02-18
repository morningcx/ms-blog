package com.morningcx.ms.blog.base;

import lombok.Data;

/**
 * @author guochenxiao
 * @date 2019/2/18
 */
@Data
public class BasicResult<T> {

    public static final int SUCCESS = 0;

    public static final int FAIL = 1;

    public static final int NO_PERMISSION = 2;

    private int code = SUCCESS;

    private String msg;

    private T data;

    public BasicResult() {}

    public BasicResult(T data) {
        this.data = data;
    }

    public BasicResult(Exception e) {
        this.msg = e.getMessage();
        this.code = FAIL;
    }
}

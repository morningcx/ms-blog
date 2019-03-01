package com.morningcx.ms.blog.base.result;

import lombok.Data;

/**
 * @author gcx
 * @date 2019/2/18
 */
@Data
public class Result {

    public static final int SUCCESS = 0;

    public static final int FAIL = 1;

    /*public static final int NO_PERMISSION = 2;*/

    /*private static final String DEFAULT_MSG = "操作成功";*/

    private int code;

    private String msg;

    private Object data;

    public Result() {}

    public static Result ok(Object data) {
        return new Result().code(SUCCESS).data(data);
    }

    public static Result fail(String msg) {
        return new Result().code(FAIL).msg(msg);
    }

    public Result code(int code) {
        this.code = code;
        return this;
    }

    public Result msg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result data(Object data) {
        this.data = data;
        return this;
    }

}

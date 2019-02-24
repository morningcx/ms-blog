package com.morningcx.ms.blog.base.result;

import lombok.Data;

/**
 * @author gcx
 * @date 2019/2/18
 */
@Data
public class BaseResult {

    public static final int SUCCESS = 0;

    public static final int FAIL = 1;

    public static final int NO_PERMISSION = 2;

    private int code = SUCCESS;

    private String msg;

    private Object data;

    public BaseResult() {}

    public BaseResult(Object data) {
        this.data = data;
    }

    public BaseResult(Exception e) {
        this.msg = e.getMessage();
        this.code = FAIL;
    }
}

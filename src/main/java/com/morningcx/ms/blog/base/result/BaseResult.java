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

    /*public static final int NO_PERMISSION = 2;*/

    private int code;

    private String msg;

    private Object data;

    public BaseResult() {}

    public static BaseResult success(Object data) {
        BaseResult result = new BaseResult();
        result.setCode(SUCCESS);
        result.setData(data);
        return result;
    }

    public static BaseResult fail(String msg) {
        BaseResult result = new BaseResult();
        result.setCode(FAIL);
        result.setMsg(msg);
        return result;
    }
}

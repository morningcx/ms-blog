package com.morningcx.ms.blog.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author guochenxiao
 * @date 2019/2/20
 */
public enum StateEnum {
    PRIMARY(0),
    SECONDORY(1),
    HIGH(2);

    @EnumValue
    private final int code;

    StateEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

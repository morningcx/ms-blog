package com.morningcx.ms.blog.base.other;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gcx
 * @date 2019/2/20
 */
@Getter
@AllArgsConstructor
public enum StateEnum {
    PRIMARY(0, "小学"),
    SECONDORY(1, "中学"),
    HIGH(2, "高中");

    @EnumValue
    private int code;
    @EnumDesc
    private String desc;
}

package com.morningcx.ms.blog.base.annotation;

import com.morningcx.ms.blog.base.enums.OpEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gcx
 * @date 2019/3/9
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    /**
     * 描述
     *
     * @return
     */
    String desc();

    /**
     * 类型
     *
     * @return
     */
    OpEnum type();
}

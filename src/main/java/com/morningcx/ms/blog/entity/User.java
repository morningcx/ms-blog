package com.morningcx.ms.blog.entity;

import lombok.Data;

/**
 * @author guochenxiao
 * @date 2019/2/16
 */
@Data
public class User {

    private Integer id;

    private String account;

    private String password;

    private String name;
}

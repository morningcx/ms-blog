package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author gcx
 * @date 2019/2/16
 */
@Data
@TableName("t_user")
public class User {

    private Integer id;
    private String account;
    @TableField(select = false)
    private String password;
    private String name;
    private String github;
    private String email;
    private String zhihu;
}

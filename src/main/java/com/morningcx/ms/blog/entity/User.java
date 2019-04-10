package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author gcx
 * @date 2019/2/16
 */
@Data
@TableName("t_user")
public class User implements Serializable {

    private Integer id;
    private String account;
    @Null(message = "不能修改密码")
    @TableField(select = false)
    private String password;
    @Size(max = 15, message = "昵称不能超过15个字符")
    @NotBlank(message = "昵称不能为空")
    private String name;
    @NotBlank(message = "个性签名不能为空")
    private String signature;
    @NotBlank(message = "头像不能为空")
    private String headImageUrl;
    @Min(value = 0, message = "性别错误")
    @Max(value = 2, message = "性别错误")
    @NotNull(message = "请选择性别")
    private Integer gender;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @NotNull(message = "生日不能为空")
    private Date birthday;
    private String qq;
    private String wechat;
    private String github;
    @NotBlank(message = "邮箱不能为空")
    private String email;
    private String zhihu;
    @Null(message = "不能修改创建时间")
    private Date createTime;
    @Null(message = "不能修改更新时间")
    private Date updateTime;
    @TableLogic
    @TableField(select = false)
    private Integer deleted;



    @TableField(exist = false)
    private Integer articleCount;
    @TableField(exist = false)
    private Integer categoryCount;
    @TableField(exist = false)
    private Integer tagCount;
}

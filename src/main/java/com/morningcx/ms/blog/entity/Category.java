package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author gcx
 * @date 2019/2/21
 */
@Data
@TableName("t_category")
public class Category {
    private Integer id;
    private Integer pid;
    private Integer userId;
    private String name;
    private String description;
    private Date createTime;
    private Date updateTime;
    @TableLogic
    private Integer deleted;
}

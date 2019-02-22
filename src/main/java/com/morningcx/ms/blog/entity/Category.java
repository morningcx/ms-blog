package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author guochenxiao
 * @date 2019/2/21
 */
@Data
@TableName("t_category")
public class Category {
    private Integer id;
    private Integer pid;
    private Integer userId;
    private String name;
    private Date createTime;
    private Date updateTime;
}

package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author guochenxiao
 * @date 2019/2/3
 */
@Data
@TableName("t_article")
public class Article {

    private Integer id;

    private Integer authorId;

    @TableField(condition = SqlCondition.LIKE)
    private String title;

    private String introduction;

    private Integer contentId;

    private Integer categoryId;

    private Date createTime;

    private Date updateTime;

    private Integer type;

    private Integer likes;

    private Integer views;

    private Integer state;
}

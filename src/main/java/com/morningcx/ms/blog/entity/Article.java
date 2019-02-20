package com.morningcx.ms.blog.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.morningcx.ms.blog.base.result.EnumJSONParser;
import com.morningcx.ms.blog.entity.enums.StateEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author guochenxiao
 * @date 2019/2/3
 */
@Data
@TableName("t_article")
@Accessors(chain = true)
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

    @JSONField(deserializeUsing = EnumJSONParser.class)
    private StateEnum state;

    @TableLogic
    private Integer deleted;
}

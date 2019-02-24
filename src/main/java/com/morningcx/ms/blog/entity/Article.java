package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author gcx
 * @date 2019/2/3
 */
@Data
@TableName("t_article")
public class Article {

    private Integer id;
    private Integer authorId;
    private String authorName;
    private String title;
    private String introduction;
    private Integer contentId;
    private Integer categoryId;
    private String categoryName;
    private Date createTime;
    private Date updateTime;
    private Integer type;
    private Integer likes;
    private Integer views;
    private Integer state;
    private Integer modifier;
    @TableLogic
    @TableField(select = false)
    private Integer deleted;


    @TableField(exist = false)
    private Content content;
    @TableField(exist = false)
    private List<Tag> tags;
}

package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author gcx
 * @date 2019/2/3
 */
@Data
@TableName("t_article")
public class Article implements Serializable {

    private Integer id;
    private Integer authorId;
    @TableField(condition = SqlCondition.LIKE)
    @NotBlank(message = "标题不能为空")
    private String title;
    @TableField(condition = SqlCondition.LIKE)
    @NotBlank(message = "简介不能为空")
    private String introduction;
    private Integer contentId;
    @NotNull(message = "分类不能为空")
    private Integer categoryId;
    private Date createTime;
    private Date updateTime;
    private Integer likes;
    private Integer views;
    @NotNull(message = "类型不能为空")
    private Integer type;
    private Integer modifier;
    private Integer recycle;
    @TableLogic
    @TableField(select = false)
    private Integer deleted;



    @TableField(exist = false)
    private String author;
    @TableField(exist = false)
    private String category;
    @TableField(exist = false)
    private Content content;
    @NotEmpty(message = "标签不能为空")
    @Size(max = 5, message = "标签不能超过5个")
    @TableField(exist = false)
    private List<String> tagNames;
}

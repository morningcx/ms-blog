package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "描述不能为空")
    private String description;
    private Date createTime;
    private Date updateTime;
    @TableLogic
    private Integer deleted;

    /*@TableField(exist = false)
    private List<Category> children;
    @TableField(exist = false)
    private List<Article> articles;*/

}

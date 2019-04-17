package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author gcx
 * @date 2019/2/21
 */
@Data
@TableName("t_category")
public class Category implements Serializable {
    private Integer id;
    private Integer pid;
    private Integer userId;
    @NotBlank(message = "封面不能为空")
    private String cover;
    @TableField(condition = SqlCondition.LIKE)
    @NotBlank(message = "名称不能为空")
    private String name;
    @TableField(condition = SqlCondition.LIKE)
    @NotBlank(message = "描述不能为空")
    private String description;
    private Date createTime;
    private Date updateTime;
    @TableLogic
    @TableField(select = false)
    private Integer deleted;

    /**
     * 分类下文章数量
     */
    @TableField(exist = false)
    private Integer count;

}

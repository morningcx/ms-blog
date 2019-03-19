package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author gcx
 * @date 2019/2/16
 */
@Data
@TableName("t_tag")
public class Tag implements Serializable {

    private Integer id;
    private String name;
    private String description;

    @TableField(exist = false)
    private Integer count;
}

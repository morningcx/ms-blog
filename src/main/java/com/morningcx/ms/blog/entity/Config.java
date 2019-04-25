package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author gcx
 * @date 2019/4/25
 */
@Data
@TableName("t_config")
public class Config {
    private Integer id;
    private Integer pid;
    private String name;
    private String keyword;
    private String value;
    private String description;
    private Date createTime;
    private Date updateTime;
}

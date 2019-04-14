package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gcx
 * @date 2019/2/28
 */
@Data
@TableName("t_image")
public class Image implements Serializable {
    private Integer id;
    private String path;
    private String domain;
    private String bucket;
    private String fkey;
    private String hash;
    private Integer size;
    private String type;
    private String name;
    private Integer width;
    private Integer height;
    private String average;
    private Date createTime;
    @TableLogic
    @TableField(select = false)
    private Integer deleted;
}

package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gcx
 * @date 2019/2/28
 */
@Data
public class Image implements Serializable {
    private Integer id;
    private String path;
    private String key;
    private String hash;
    private String bucket;
    private Integer size;
    private String type;
    private String name;
    private Integer width;
    private Integer height;
    private String average;
    private Integer uploaderId;
    private Date uploadTime;
    @TableLogic
    @TableField(select = false)
    private Integer deleted;
}

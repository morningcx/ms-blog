package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author gcx
 * @date 2019/2/16
 */
@Data
@TableName("t_tag")
public class Tag {

    private Integer id;
    private String name;
}

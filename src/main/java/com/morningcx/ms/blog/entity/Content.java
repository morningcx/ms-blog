package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author gcx
 * @date 2019/2/19
 */
@Data
@TableName("t_content")
public class Content {
    private Integer id;
    private String content;
}

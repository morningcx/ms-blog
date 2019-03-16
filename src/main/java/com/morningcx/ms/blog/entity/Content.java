package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author gcx
 * @date 2019/2/19
 */
@Data
@TableName("t_content")
public class Content implements Serializable {
    private Integer id;
    private String content;
}

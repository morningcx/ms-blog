package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author gcx
 * @date 2019/2/16
 */
@Data
@TableName("t_tag")
public class Tag implements Serializable {

    private Integer id;
    @NotBlank(message = "标签名称不能为空")
    private String name;
    private String description;
}

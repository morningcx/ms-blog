package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author guochenxiao
 * @date 2019/2/17
 */
@Data
@TableName("t_operation_log")
public class OperationLog implements Serializable {
    private Integer id;
    private Integer userId;
    private String ip;
    private String module;
    private String type;
    @TableField(condition = SqlCondition.LIKE)
    private String content;
    private String agent;
    private String browser;
    private String os;
    private String device;
    private String country;
    private String province;
    private String city;
    private String isp;
    private String url;
    private Date time;
    private String method;
    private Long cost;
    private String session;
}

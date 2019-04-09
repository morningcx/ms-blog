package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gcx
 * @date 2019/4/8
 */
@Data
@TableName("t_access_log")
public class AccessLog implements Serializable {
    private Integer id;
    private String ip;
    private String module;
    private String type;
    private String content;
    private String agent;
    private String browser;
    private String os;
    private String country;
    private String province;
    private String city;
    private String isp;
    private String url;
    private String method;
    private Date time;
    private Long cost;
}
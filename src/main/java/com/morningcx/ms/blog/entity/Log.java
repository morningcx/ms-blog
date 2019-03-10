package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author guochenxiao
 * @date 2019/2/17
 */
@Data
@TableName("t_log")
public class Log {
    private Integer id;
    private String ip;
    private String module;
    private String type;
    private String content;
    private String agent;
    private String url;
    private Date time;
    private String method;
    private Long cost;
    private String session;
}

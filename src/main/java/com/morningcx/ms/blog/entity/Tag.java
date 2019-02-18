package com.morningcx.ms.blog.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author guochenxiao
 * @date 2019/2/16
 */
@Data
@Entity
@Table(name = "t_tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tag;
}

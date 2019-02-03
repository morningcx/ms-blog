package com.morningcx.ms.blog.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author guochenxiao
 * @date 2019/2/3
 */
@Data
@Entity
@Table(name = "t_article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;
}

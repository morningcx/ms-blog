package com.morningcx.ms.blog.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Entity 是一个必选的注解，声明这个类对应了一个数据库表。
 * @Table(name = "AUTH_USER") 是一个可选的注解。声明了数据库实体对应的表信息。
 * 包括表名称、索引信息等。这里声明这个实体类对应的表名是 AUTH_USER。
 * 如果没有指定，则表名和实体的名称保持一致。
 *
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

    private Integer author;

    private String title;

    private String introduction;

    /**
     * @Lob 代表是长字段类型，默认的话，是longtext类型，所以需要下面这个属性来指定对应的类型。
     * columnDefinition="text"里面的类型可以随意改，后面mysql可能会有新的类型，
     * 只要是对应java的String类型，就可以在这里动态配置。
     */
    @Lob
    @Column(columnDefinition="text")
    @Basic(fetch = FetchType.LAZY)
    private String content;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private Integer type;

    private Integer like;

    private Integer views;

    private Integer state;





    /*@OneToOne
    @JoinColumn(name = "author")
    *//*NotFound : 意思是找不到引用的外键数据时忽略，NotFound默认是exception*//*
    @NotFound(action = NotFoundAction.IGNORE)
    // 默认为'article0_.author_id'
    private User author;*/
}

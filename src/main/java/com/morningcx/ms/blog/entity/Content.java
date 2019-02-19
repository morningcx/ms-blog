package com.morningcx.ms.blog.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author guochenxiao
 * @date 2019/2/19
 */
@Data
@Entity
@Table(name = "t_content")
public class Content {

    private Integer id;

    /**
     * @Lob 代表是长字段类型，默认的话，是longtext类型，所以需要下面这个属性来指定对应的类型。
     * columnDefinition="text"里面的类型可以随意改，后面mysql可能会有新的类型，
     * 只要是对应java的String类型，就可以在这里动态配置。
     */
    @Lob
    @Column(columnDefinition="text")
    private String content;
}

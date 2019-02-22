package com.morningcx.ms.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author guochenxiao
 * @date 2019/2/21
 */
@Data
@TableName("t_article_tag")
public class ArticleTag {
    private Integer id;
    private Integer articleId;
    private Integer tagId;
}

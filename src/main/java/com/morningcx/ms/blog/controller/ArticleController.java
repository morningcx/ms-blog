package com.morningcx.ms.blog.controller;

import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.jpa.ArticleJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guochenxiao
 * @date 2019/2/3
 */
@RestController
public class ArticleController {
    @Autowired
    private ArticleJpa articleJpa;

    @PostMapping("insertArticle")
    public Article insertArticle(Article article) {
        articleJpa.save(article);
        return article;
    }

}

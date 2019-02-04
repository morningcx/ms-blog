package com.morningcx.ms.blog.controller;

import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.jpa.ArticleJpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guochenxiao
 * @date 2019/2/3
 */
@Slf4j
@RestController
public class ArticleController {
    @Autowired
    private ArticleJpa articleJpa;

    @PostMapping("insertArticle")
    public Article insertArticle(Article article) {
        articleJpa.save(article);
        return article;
    }

    @GetMapping("getArticle")
    public Article getArticle(@RequestParam Integer id) {
        return articleJpa.findById(id).orElse(null);
    }

    @PostMapping("updateArticle")
    public Article updateArticle(Article article, String test) {
        log.info(test);
        return articleJpa.save(article);
    }
}

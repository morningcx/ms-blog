package com.morningcx.ms.blog.web.controller;

import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.web.service.WebArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/3/27
 */
@CacheConfig(cacheNames = "article")
@RestController
@RequestMapping(path = "web/article", name = "文章")
public class WebArticleController {

    @Autowired
    private WebArticleService webArticleService;

    /*@Cacheable*/
    @GetMapping("listArticle")
    public Result listArticle(Article article, Integer page, Integer limit) {
        return Result.ok(webArticleService.listArticle(article, page, limit));
    }

    @GetMapping("listHotArticles")
    public Result listHotArticles(Integer userId) {
        return Result.ok(webArticleService.listHotArticles(userId));
    }

    /*@Cacheable*/
    @GetMapping("getFullById")
    public Result getFullById(Integer id) {
        return Result.ok(webArticleService.getFullById(id));
    }

    @PostMapping("like")
    public Result updateLikes(Integer id) {
        return Result.ok(webArticleService.updateLikesById(id));
    }


}

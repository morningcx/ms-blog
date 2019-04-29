package com.morningcx.ms.blog.controller.web;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.service.web.WebArticleService;
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
    @Log(type = LogTypeEnum.PAGE, desc = "分页查询文章列表[页码：{page}, 页量：{limit}]")
    @GetMapping("listArticle")
    public Result listArticle(Article article, Integer page, Integer limit) {
        return Result.ok(webArticleService.listArticle(article, page, limit));
    }

    @Log(type = LogTypeEnum.PAGE, desc = "分页查询热门文章列表[页码：{page}, 页量：{limit}]")
    @GetMapping("listHotArticles")
    public Result listHotArticles(Article article, Integer page, Integer limit) {
        return Result.ok(webArticleService.listHotArticles(article, page, limit));
    }

    @Log(type = LogTypeEnum.PAGE, desc = "分页查询简单文章列表[页码：{page}, 页量：{limit}]")
    @GetMapping("listSimpleArticles")
    public Result listSimpleArticles(Integer page, Integer limit) {
        return Result.ok(webArticleService.listSimpleArticles(page, limit));
    }

    @Log(type = LogTypeEnum.PAGE, desc = "分页查询文章归档列表[页码：{page}, 页量：{limit}]")
    @GetMapping("listArchives")
    public Result listArchives(Integer page, Integer limit) {
        return Result.ok(webArticleService.listArchives(page, limit));
    }

    @Log(type = LogTypeEnum.Query, desc = "查询推荐文章")
    @GetMapping("listRecommendArticles")
    public Result listRecommendArticles() {
        return Result.ok(webArticleService.listRecommendArticles());
    }

    /*@Cacheable*/
    @Log(type = LogTypeEnum.Query, desc = "浏览文章[id:{id}]")
    @GetMapping("getFullById")
    public Result getFullById(Integer id) {
        return Result.ok(webArticleService.getFullById(id));
    }

    @Log(type = LogTypeEnum.Query, desc = "喜欢文章[id:{id}]")
    @PostMapping("like")
    public Result updateLikes(Integer id) {
        return Result.ok(webArticleService.updateLikesById(id));
    }


}

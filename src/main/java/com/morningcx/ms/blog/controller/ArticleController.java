package com.morningcx.ms.blog.controller;

import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.base.util.SelectUtil;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author gcx
 * @date 2019/2/3
 */
@Slf4j
@RestController
@RequestMapping("article")
public class ArticleController {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;

    @GetMapping("getFullById")
    public Article getFullById(Integer id) {
        return articleService.getArticleById(id);
    }

    @GetMapping("getMiniById")
    public Article getMiniById(Integer id) {
        return articleMapper.selectOne(SelectUtil.baseWrapper().eq("id", id));
    }


    @PostMapping("insertArticle")
    public Result insertArticle(@Valid Article article) {
        return Result.ok(articleService.insertArticle(article));
    }

    @GetMapping("getArticlesByCondition")
    public List<Article> getArticlesByCondition(Article article, Integer curr, Integer size) {
        /*QueryWrapper<Article> queryWrapper = new QueryWrapper<>(article);
        return articleMapper.selectPage(
                new Page<>(curr == null ? 1 : curr, size == null ? 10 : size),
                queryWrapper).getRecords();*/
        return null;
    }

    @PostMapping("deleteArticle")
    public Integer deleteArticle(Integer id) {
        return null;
    }

    @PostMapping("updateArticle")
    public Article updateArticle(Article article) {
        return article;
    }


}

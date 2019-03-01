package com.morningcx.ms.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author gcx
 * @date 2019/2/3
 */
@Slf4j
@RestController
public class ArticleController {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;

    @GetMapping("getArticleById")
    public Article getArticleById(Integer id) {
        return articleService.getArticleById(id);
    }


    @PostMapping("insertArticle")
    public Result insertArticle(@Valid Article article) {
        return Result.ok(articleService.insertArticle(article));
    }

    @GetMapping("getArticlesByCondition")
    public List<Article> getArticlesByCondition(Article article, Integer curr, Integer size) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>(article);
        return articleMapper.selectPage(
                new Page<>(curr == null ? 1 : curr, size == null ? 10 : size),
                queryWrapper).getRecords();
    }

    @PostMapping("deleteArticle")
    public Integer deleteArticle(Integer id) {
        return articleMapper.deleteById(id);
    }
    /*@GetMapping("getUser")
    public User getUser(Integer id) {
        BusinessException.cause("我测试的");

    }*/

    /*@GetMapping("findByAuthor")
    public List<Article> findByAuthor(Integer author) {
        return articleJpa.findByAuthor(author);
    }*/

    @PostMapping("updateArticle")
    public Article updateArticle(Article article, String test) {
        log.info(test);
        articleMapper.updateById(article);
        return article;
    }


}

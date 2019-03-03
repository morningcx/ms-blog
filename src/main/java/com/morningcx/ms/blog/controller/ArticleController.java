package com.morningcx.ms.blog.controller;

import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.base.util.SelectUtil;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import com.morningcx.ms.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("listArticlesByCondition")
    public Result listArticlesByCondition(Article article, Integer page, Integer limit) {
        return Result.ok(articleService.listArticlesByCondition(article, page, limit));
    }

    @GetMapping("listRecycleBin")
    public Result listRecycleBin(Integer page, Integer limit) {
        return Result.ok(articleService.listRecycleBin(page, limit));
    }

    @PostMapping("recycleArticle")
    public Result recycleArticle(@RequestBody List<Integer> recycleIds) {
        return Result.ok(articleService.recycleArticle(recycleIds));
    }

    @PostMapping("recoverArticle")
    public Result recoverArticle(@RequestBody List<Integer> recoverIds) {
        return Result.ok(articleService.recoverArticle(recoverIds));
    }

    @PostMapping("deleteArticle")
    public Result deleteArticle(@RequestBody List<Integer> deleteIds) {
        return Result.ok(articleService.deleteArticle(deleteIds));
    }

}

package com.morningcx.ms.blog.controller;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author gcx
 * @date 2019/2/3
 */
@RestController
@RequestMapping(path = "article", name = "文章")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Log(type = LogTypeEnum.READ, desc = "根据ID查询文章元信息")
    @GetMapping("getMetaById")
    public Result getMetaById(Integer id) {
        return Result.ok(articleService.getMetaById(id));
    }

    @Log(type = LogTypeEnum.UPDATE, desc = "修改文章元信息")
    @PostMapping("updateMeta")
    public Result updateMeta(@Valid @RequestBody Article article) {
        return Result.ok(articleService.updateMeta(article));
    }

    @Log(type = LogTypeEnum.UPDATE, desc = "修改ID{id}文章的修饰符为{modifier}")
    @PostMapping("updateModifier")
    public Result updateModifier(Integer id, Integer modifier) {
        return Result.ok(articleService.updateModifier(id, modifier));
    }

    @Log(type = LogTypeEnum.CREATE, desc = "新增文章")
    @PostMapping("insertArticle")
    public Result insertArticle(@RequestBody @Valid Article article) {
        return Result.ok(articleService.insertArticle(article));
    }

    @Log(type = LogTypeEnum.PAGE, desc = "分页查询文章")
    @GetMapping("listArticle")
    public Result listArticle(Article article, Integer page, Integer limit) {
        return Result.ok(articleService.listArticle(article, page, limit));
    }

    @Log(type = LogTypeEnum.PAGE, desc = "分页查询回收站")
    @GetMapping("listRecycleBin")
    public Result listRecycleBin(Article article, Integer page, Integer limit) {
        return Result.ok(articleService.listRecycleBin(article, page, limit));
    }

    @Log(type = LogTypeEnum.RECYCLE, desc = "回收文章")
    @PostMapping("recycleArticle")
    public Result recycleArticle(@RequestBody List<Integer> recycleIds) {
        return Result.ok(articleService.recycleArticle(recycleIds));
    }

    @Log(type = LogTypeEnum.RECOVER, desc = "恢复文章")
    @PostMapping("recoverArticle")
    public Result recoverArticle(@RequestBody List<Integer> recoverIds) {
        return Result.ok(articleService.recoverArticle(recoverIds));
    }

    @Log(type = LogTypeEnum.DELETE, desc = "彻底删除文章")
    @PostMapping("deleteArticle")
    public Result deleteArticle(@RequestBody List<Integer> deleteIds) {
        return Result.ok(articleService.deleteArticle(deleteIds));
    }
}

package com.morningcx.ms.blog.controller;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.OpEnum;
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

    @Log(type = OpEnum.READ, desc = "根据ID查询文章元信息")
    @GetMapping("getMetaById")
    public Result getMetaById(Integer id) {
        return Result.ok(articleService.getMetaById(id));
    }

    @Log(type = OpEnum.UPDATE, desc = "修改文章元信息")
    @PostMapping("updateMeta")
    public Result updateMeta(@Valid @RequestBody Article article) {
        return Result.ok(articleService.updateMeta(article));
    }

    @Log(type = OpEnum.CREATE, desc = "新增文章")
    @PostMapping("insertArticle")
    public Result insertArticle(@RequestBody @Valid Article article) {
        return Result.ok(articleService.insertArticle(article));
    }

    @Log(type = OpEnum.READ, desc = "分页查询文章")
    @GetMapping("listArticlesByCondition")
    public Result listArticlesByCondition(Article article, Integer page, Integer limit) {
        return Result.ok(articleService.listArticlesByCondition(article, page, limit));
    }

    @Log(type = OpEnum.READ, desc = "分页查询回收站")
    @GetMapping("listRecycleBin")
    public Result listRecycleBin(Integer page, Integer limit) {
        return Result.ok(articleService.listRecycleBin(page, limit));
    }

    @Log(type = OpEnum.RECYCLE, desc = "回收文章")
    @PostMapping("recycleArticle")
    public Result recycleArticle(@RequestBody List<Integer> recycleIds) {
        return Result.ok(articleService.recycleArticle(recycleIds));
    }

    @Log(type = OpEnum.RECOVER, desc = "恢复文章")
    @PostMapping("recoverArticle")
    public Result recoverArticle(@RequestBody List<Integer> recoverIds) {
        return Result.ok(articleService.recoverArticle(recoverIds));
    }

    @Log(type = OpEnum.DELETE, desc = "彻底删除文章")
    @PostMapping("deleteArticle")
    public Result deleteArticle(@RequestBody List<Integer> deleteIds) {
        return Result.ok(articleService.deleteArticle(deleteIds));
    }
}

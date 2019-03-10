package com.morningcx.ms.blog.controller;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/3/10
 */
@RestController
@RequestMapping(path = "content", name = "内容")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Log(type = "查询", desc = "根据文章ID查询内容和标题")
    @GetMapping("getByArticleId")
    public Result getByArticleId(Integer articleId) {
        return Result.ok(contentService.getByArticleId(articleId));
    }

    @Log(type = "修改", desc = "根据文章ID修改内容")
    @PostMapping("updateByArticleId")
    public Result updateByArticleId(Integer articleId, String content) {
        return Result.ok(contentService.updateByArticleId(articleId, content));
    }
}

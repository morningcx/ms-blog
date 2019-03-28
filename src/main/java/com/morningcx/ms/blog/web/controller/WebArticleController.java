package com.morningcx.ms.blog.web.controller;

import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.web.service.WebArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/3/27
 */
@RestController
@RequestMapping(path = "web/article", name = "文章")
public class WebArticleController {

    @Autowired
    private WebArticleService webArticleService;

    @GetMapping("listHotArticles")
    public Result listHotArticles(Integer userId) {
        return Result.ok(webArticleService.listHotArticles(userId));
    }

    @GetMapping("getFullById")
    public Result getFullById(Integer id) {
        return Result.ok(webArticleService.getFullById(id));
    }

}

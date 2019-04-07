package com.morningcx.ms.blog.web.controller;

import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.web.service.WebCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/4/7
 */
@RestController
@RequestMapping(path = "web/category", name = "分类")
public class WebCategoryController {

    @Autowired
    private WebCategoryService webCategoryService;

    @GetMapping("listAll")
    public Result listAll(Integer userId) {
        return Result.ok(webCategoryService.listAll(userId));
    }
}

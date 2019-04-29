package com.morningcx.ms.blog.controller.web;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.service.web.WebCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Log(type = LogTypeEnum.Query, desc = "查询分类[id:{id}]")
    @GetMapping("{id}")
    public Result getById(@PathVariable("id") Integer id) {
        return Result.ok(webCategoryService.getById(id));
    }

    @Log(type = LogTypeEnum.Query, desc = "查询所有分类")
    @GetMapping("listAll")
    public Result listAll() {
        return Result.ok(webCategoryService.listAll());
    }

    @Log(type = LogTypeEnum.Query, desc = "查询推荐分类")
    @GetMapping("listRecommendCategories")
    public Result listRecommendCategories() {
        return Result.ok(webCategoryService.listRecommendCategories());
    }
}

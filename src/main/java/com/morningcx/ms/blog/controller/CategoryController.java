package com.morningcx.ms.blog.controller;

import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.Category;
import com.morningcx.ms.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author gcx
 * @date 2019/3/8
 */
@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("listPage")
    public Result listPage(Category category, Integer page, Integer limit) {
        return Result.ok(categoryService.listPage(category, page, limit));
    }

    @PostMapping("insert")
    public Result insert(@Valid Category category) {
        return Result.ok(categoryService.insert(category));
    }

}

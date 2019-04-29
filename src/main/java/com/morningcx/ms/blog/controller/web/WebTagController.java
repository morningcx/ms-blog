package com.morningcx.ms.blog.controller.web;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.service.web.WebTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/4/18
 */
@RestController
@RequestMapping(path = "web/tag", name = "标签")
public class WebTagController {
    @Autowired
    private WebTagService webTagService;

    @Log(type = LogTypeEnum.Query, desc = "查询标签[id:{id}]")
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.ok(webTagService.getById(id));
    }

    @Log(type = LogTypeEnum.Query, desc = "查询所有标签")
    @GetMapping("listAll")
    public Result listAll() {
        return Result.ok(webTagService.listAll());
    }
}

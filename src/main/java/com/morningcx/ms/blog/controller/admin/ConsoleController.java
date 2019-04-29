package com.morningcx.ms.blog.controller.admin;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.service.admin.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/4/22
 */
@RestController
@RequestMapping(path = "console", name = "控制台")
public class ConsoleController {
    @Autowired
    private ConsoleService consoleService;

    @GetMapping("getModuleCount")
    @Log(type = LogTypeEnum.Query, desc = "获取控制台模块统计数据")
    public Result getModuleCount() {
        return Result.ok(consoleService.getModuleCount());
    }


    @GetMapping("listTopViewArticle")
    @Log(type = LogTypeEnum.Query, desc = "获取控制台文章点击排行")
    public Result listTopViewArticle() {
        return Result.ok(consoleService.listTopViewArticle());
    }
}

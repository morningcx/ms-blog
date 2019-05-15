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

    @GetMapping("getBrowserDistribution")
    @Log(type = LogTypeEnum.Query, desc = "获取访客浏览器分布")
    public Result getBrowserDistribution() {
        return Result.ok(consoleService.getBrowserDistribution());
    }

    @GetMapping("getOsDistribution")
    @Log(type = LogTypeEnum.Query, desc = "获取访客操作系统分布")
    public Result getOsDistribution() {
        return Result.ok(consoleService.getOsDistribution());
    }

    @GetMapping("getChinaDistribution")
    @Log(type = LogTypeEnum.Query, desc = "获取国内访客地理位置分布")
    public Result getChinaDistribution() {
        return Result.ok(consoleService.getChinaDistribution());
    }

    @GetMapping("getVisitorCount")
    @Log(type = LogTypeEnum.Query, desc = "获取一周内访客数量")
    public Result getVisitorCount() {
        return Result.ok(consoleService.getVisitorCount());
    }
}

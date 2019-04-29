package com.morningcx.ms.blog.controller.admin;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.service.admin.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author gcx
 * @date 2019/4/25
 */
@RestController
@RequestMapping(path = "config", name = "配置")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping("listConfigParent")
    @Log(type = LogTypeEnum.Query, desc = "获取所有配置组信息")
    public Result listConfigParent() {
        return Result.ok(configService.listConfigParent());
    }

    @GetMapping("listConfigGroup")
    @Log(type = LogTypeEnum.Query, desc = "获取关键字{keyword}下的所有配置信息")
    public Result listConfigGroup(String keyword) {
        return Result.ok(configService.listConfigGroupByParentKeyword(keyword));
    }

    @PostMapping("updateConfigGroup")
    @Log(type = LogTypeEnum.UPDATE, desc = "更新配置信息")
    public Result updateConfigGroup(@RequestBody Map<String, String> configMap) {
        return Result.ok(configService.updateConfigGroupByKeyword(configMap));
    }

}

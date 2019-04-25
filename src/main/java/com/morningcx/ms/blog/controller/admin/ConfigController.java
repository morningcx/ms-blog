package com.morningcx.ms.blog.controller.admin;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.service.admin.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @PostMapping("test")
    public Map<String, Object> sss(@RequestBody Map<String, String> map) {
        Map<String, Object> map2 = new HashMap<>();
        map2.put("title", 123);
        return map2;
    }

    @GetMapping("getConfigGroup")
    @Log(type = LogTypeEnum.READ, desc = "获取关键字{keyword}下的所有配置信息")
    public Result getConfigGroup(String keyword) {
        return Result.ok(configService.getConfigGroupByParentKeyword(keyword));
    }


}

package com.morningcx.ms.blog.controller.web;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.service.web.WebConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/4/26
 */
@RestController
@RequestMapping(path = "web/config", name = "配置")
public class WebConfigController {
    @Autowired
    private WebConfigService webConfigService;

    @Log(type = LogTypeEnum.READ, desc = "查询网站信息")
    @GetMapping("getWebsiteConfig")
    public Result getWebsiteConfig() {
        return Result.ok(webConfigService.getWebsiteConfig());
    }
}

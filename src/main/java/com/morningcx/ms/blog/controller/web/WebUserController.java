package com.morningcx.ms.blog.controller.web;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.service.web.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取用户和空间信息
 *
 * @author gcx
 * @date 2019/4/9
 */
@Cacheable
@CacheConfig(cacheNames = "user")
@RestController
@RequestMapping(path = "web/user", name = "用户")
public class WebUserController {

    @Autowired
    private WebUserService webUserService;

    @Log(type = LogTypeEnum.Query, desc = "查询用户信息[id:{id}]")
    @GetMapping("{id}")
    public Result getUserInfo(@PathVariable("id") Integer id) {
        return Result.ok(webUserService.getUserInfo(id));
    }
 }

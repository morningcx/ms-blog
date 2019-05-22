package com.morningcx.ms.blog.controller.admin;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.AccessLog;
import com.morningcx.ms.blog.service.admin.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/4/29
 */
@RestController
@RequestMapping(path = "accessLog", name = "请求日志")
public class AccessLogController {
    @Autowired
    private AccessLogService accessLogService;

    @Log(type = LogTypeEnum.PAGE, desc = "分页查询请求日志 页码：{page} 页量：{limit}")
    @GetMapping("listPage")
    public Result listPage(AccessLog accessLog, Integer page, Integer limit) {
        return Result.ok(accessLogService.listPage(accessLog, page, limit));
    }

    @Log(type = LogTypeEnum.PAGE, desc = "分页查询文章{articleId}日志{type} 页码：{page} 页量：{limit}")
    @GetMapping("listArticleLog")
    public Result listArticleLog(Integer page, Integer limit, Integer articleId, Integer type) {
        return Result.ok(accessLogService.listArticleLog(page, limit, articleId, type));
    }

    @Log(type = LogTypeEnum.DISCOVERY, desc = "IP探活：{ip}")
    @GetMapping("isAlive")
    public Result isAlive(String ip) throws Exception {
        return Result.ok(accessLogService.isAlive(ip));
    }

}

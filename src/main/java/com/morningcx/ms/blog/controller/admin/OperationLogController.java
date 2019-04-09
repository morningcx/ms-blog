package com.morningcx.ms.blog.controller.admin;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.OperationLog;
import com.morningcx.ms.blog.service.admin.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/4/8
 */
@RestController
@RequestMapping(path = "operationLog", name = "操作日志")
public class OperationLogController {
    @Autowired
    private OperationLogService operationLogService;

    @Log(type = LogTypeEnum.PAGE, desc = "页码：{page} 页量：{limit}")
    @GetMapping("listPage")
    public Result listPage(OperationLog operationLog, Integer page, Integer limit) {
        return Result.ok(operationLogService.listPage(operationLog, page, limit));
    }
}

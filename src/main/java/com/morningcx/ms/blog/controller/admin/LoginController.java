package com.morningcx.ms.blog.controller.admin;

import com.morningcx.ms.blog.base.annotation.FreeAuth;
import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.service.admin.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/2/27
 */
@RestController
@RequestMapping(name = "登录")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @FreeAuth
    @PostMapping("login")
    @Log(type = LogTypeEnum.LOGIN, desc = "用户登录")
    public Result login(String account, String password) {
        return Result.ok(loginService.login(account, password));
    }
}

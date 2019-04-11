package com.morningcx.ms.blog.controller.admin;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.LogTypeEnum;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.entity.User;
import com.morningcx.ms.blog.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户信息
 *
 * @author gcx
 * @date 2019/4/10
 */
@RestController
@RequestMapping(path = "user", name = "用户")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("getMyInfo")
    @Log(type = LogTypeEnum.READ, desc = "获取个人基本信息")
    public Result getMyInfo() {
        return Result.ok(userService.getMyInfo());
    }

    @PostMapping("updateMyInfo")
    @Log(type = LogTypeEnum.UPDATE, desc = "更新个人基本信息")
    public Result updateMyInfo(@Valid User user) {
        return Result.ok(userService.updateMyInfo(user));
    }

    @PostMapping("resetMyPassword")
    @Log(type = LogTypeEnum.UPDATE, desc = "重置个人密码")
    public Result resetMyPassword(String oldPassword, String newPassword) {
        return Result.ok(userService.resetMyPassword(oldPassword, newPassword));
    }
}

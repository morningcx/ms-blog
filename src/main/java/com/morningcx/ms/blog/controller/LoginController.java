package com.morningcx.ms.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.annotation.FreeAuth;
import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.enums.OpEnum;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.entity.User;
import com.morningcx.ms.blog.mapper.UserMapper;
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
    private UserMapper userMapper;

    @FreeAuth
    @PostMapping("login")
    @Log(type = OpEnum.LOGIN, desc = "用户登录")
    public Result login(String account, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().
                select("id", "password").
                eq("account", account));
        boolean correct = user != null && user.getPassword().equals(password);
        BizException.throwIf(!correct, "用户名或密码错误");
        ContextUtil.setLoginId(user.getId());
        return Result.ok(user.getId());
    }
}

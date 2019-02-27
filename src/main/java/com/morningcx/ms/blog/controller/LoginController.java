package com.morningcx.ms.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BusinessException;
import com.morningcx.ms.blog.base.result.Result;
import com.morningcx.ms.blog.base.util.RequestUtil;
import com.morningcx.ms.blog.entity.User;
import com.morningcx.ms.blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcx
 * @date 2019/2/27
 */
@RestController
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("login")
    public Result login(String account, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().
                select("id", "account", "password", "name").
                eq("account", account));
        boolean correct = user != null && user.getPassword().equals(password);
        BusinessException.throwIf(!correct, "用户名或密码错误");
        user.setPassword(null);
        RequestUtil.setCurrentUser(user);
        return Result.success(user);
    }
}

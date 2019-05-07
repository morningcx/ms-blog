package com.morningcx.ms.blog.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.entity.User;
import com.morningcx.ms.blog.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gcx
 * @date 2019/4/11
 */
@Slf4j
@Service
public class LoginService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * sha1(sha1(password + '~$m#s%a@l%t&=') + account.hashCode())
     *
     * @param account
     * @param password
     * @return
     */
    public Integer login(String account, String password) {
        BizException.throwIfBlank(account, "账号不能为空");
        BizException.throwIfBlank(password, "密码不能为空");
        User user = userMapper.selectOne(new QueryWrapper<User>().
                select("id", "password").
                eq("account", account));
        log.info("加密之前");
        boolean correct = user != null && user.getPassword().equals(
                DigestUtils.sha1Hex(password + account.hashCode()));
        log.info("加密之后");
        BizException.throwIf(!correct, "用户名或密码错误");
        ContextUtil.setLoginId(user.getId());
        log.info("loginId之后");
        return user.getId();
    }
}

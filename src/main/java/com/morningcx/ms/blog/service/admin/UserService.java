package com.morningcx.ms.blog.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.entity.User;
import com.morningcx.ms.blog.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author gcx
 * @date 2019/2/21
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public User getMyInfo() {
        return userMapper.selectById(ContextUtil.getLoginId());
    }

    /**
     * 更新当前登录用户信息
     *
     * @param user
     * @return
     */
    public Integer updateMyInfo(User user) {
        User updateUser = new User();
        // 当前登录用户id
        updateUser.setId(ContextUtil.getLoginId());
        // 必填项
        updateUser.setHeadImageUrl(user.getHeadImageUrl());
        updateUser.setName(user.getName());
        updateUser.setGender(user.getGender());
        updateUser.setBirthday(user.getBirthday());
        updateUser.setSignature(user.getSignature());
        // 选填
        updateUser.setQq(user.getQq());
        updateUser.setWechat(user.getWechat());
        updateUser.setGithub(user.getGithub());
        updateUser.setZhihu(user.getZhihu());
        // 设置更新时间
        updateUser.setUpdateTime(new Date());
        return userMapper.updateById(updateUser);
    }

    /**
     * 重置密码
     *
     * @return
     */
    public Integer resetMyPassword(String oldPassword, String newPassword) {
        BizException.throwIfBlank(oldPassword, "原密码不能为空");
        BizException.throwIfBlank(newPassword, "新密码不能为空");
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("id", ContextUtil.getLoginId())
                .select("id", "account", "password"));
        int accountHash = user.getAccount().hashCode();
        String oldHex = DigestUtils.sha1Hex(oldPassword + accountHash);
        BizException.throwIf(!user.getPassword().equals(oldHex), "原密码不正确");
        String newHex = DigestUtils.sha1Hex(newPassword + accountHash);
        user.setAccount(null);
        user.setPassword(newHex);
        return userMapper.updateById(user);
    }
}

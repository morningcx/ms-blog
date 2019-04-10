package com.morningcx.ms.blog.service.admin;

import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.entity.User;
import com.morningcx.ms.blog.mapper.UserMapper;
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
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public Integer updateMyInfo(User user) {
        user.setId(ContextUtil.getLoginId());
        user.setAccount(null);
        // 设置修改时间
        user.setUpdateTime(new Date());
        return userMapper.updateById(user);
    }

}

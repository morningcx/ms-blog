package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morningcx.ms.blog.base.exception.BaseException;
import com.morningcx.ms.blog.entity.User;
import com.morningcx.ms.blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author guochenxiao
 * @date 2019/2/21
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 通过id获取用户信息
     * @param id
     * @param loadDetail 是否加载详细信息
     * @return
     */
    public User getUserById(Integer id, boolean loadDetail) {
        User user;
        if (loadDetail) {
            user = userMapper.selectById(id);
            user.setPassword(null);
        } else {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            // 只加载id和昵称
            wrapper.select("id", "name").eq("id", id);
            user = userMapper.selectOne(wrapper);
        }
        return user;
    }

    @Transactional
    public void sss() {
        User user = new User();
        user.setAccount("asdsad");

        userMapper.insert(user);
        System.out.println(user.getId());
        BaseException.throwBy("test");
    }

}

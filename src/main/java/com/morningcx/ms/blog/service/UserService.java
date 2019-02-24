package com.morningcx.ms.blog.service;

import com.morningcx.ms.blog.entity.User;
import com.morningcx.ms.blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gcx
 * @date 2019/2/21
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 通过id获取用户信息
     * @param id
     * @return
     */
    public User getUserById(Integer id) {
        return null;
    }

}

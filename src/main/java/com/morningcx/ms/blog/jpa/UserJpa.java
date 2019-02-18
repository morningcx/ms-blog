package com.morningcx.ms.blog.jpa;

import com.morningcx.ms.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author guochenxiao
 * @date 2019/2/16
 */
public interface UserJpa extends JpaRepository<User, Integer> {
}

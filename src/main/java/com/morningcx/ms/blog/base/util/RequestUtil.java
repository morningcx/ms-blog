package com.morningcx.ms.blog.base.util;

import com.morningcx.ms.blog.base.exception.BusinessException;
import com.morningcx.ms.blog.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author gcx
 * @date 2019/2/27
 */
public class RequestUtil {

    private static final String LOGIN_USER_INFO = "loginUserInfo";

    /**
     * 获取当前请求属性
     *
     * @return
     */
    public static ServletRequestAttributes getCurrentAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static User getCurrentUser() {
        HttpSession session = getCurrentAttributes().getRequest().getSession();
        User user = (User) session.getAttribute(LOGIN_USER_INFO);
        BusinessException.throwNull(user, "未登录");
        return user;
    }

    /**
     * 保存登录用户
     *
     * @param user
     */
    public static void setCurrentUser(User user) {
        getCurrentAttributes().getRequest().getSession().setAttribute(LOGIN_USER_INFO, user);
    }
}

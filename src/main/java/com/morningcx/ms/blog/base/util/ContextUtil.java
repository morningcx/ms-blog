package com.morningcx.ms.blog.base.util;

import com.morningcx.ms.blog.base.exception.BizException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author gcx
 * @date 2019/2/27
 */
public class ContextUtil {

    public static final String LOGIN_USER_ID = "loginId";

    /**
     * 获取当前请求属性
     *
     * @return
     */
    public static ServletRequestAttributes getAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取当前登录用户的id
     *
     * @return
     */
    public static Integer getLoginId() {
        HttpSession s = getAttributes().getRequest().getSession(false);
        Object loginId = null;
        BizException.throwIf(s == null || (loginId = s.getAttribute(LOGIN_USER_ID)) == null, "未登录");
        return (Integer) loginId;
    }

    /**
     * 保存登录用户的id
     *
     * @param loginId
     */
    public static void setLoginId(Integer loginId) {
        getAttributes().getRequest().getSession().setAttribute(LOGIN_USER_ID, loginId);
    }
}

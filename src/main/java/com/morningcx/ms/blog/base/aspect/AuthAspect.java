package com.morningcx.ms.blog.base.aspect;

import com.morningcx.ms.blog.base.annotation.FreeAuth;
import com.morningcx.ms.blog.base.util.ContextUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 登录认证
 *
 * @author gcx
 * @date 2019/3/4
 */
@Aspect
@Component
@Order(1)
public class AuthAspect {

    /**
     * 1、execution(): 表达式主体。
     * <p>
     * 2、第一个*号：表示返回类型，*号表示所有的类型。
     * <p>
     * 3、包名：表示需要拦截的包名，后面的两个句点表示当前包和当前包的所有子包，com.sample.service.impl包、子孙包下所有类的方法。
     * <p>
     * 4、第二个*号：表示类名，*号表示所有的类。
     * <p>
     * 5、*(..):最后这个星号表示方法名，*号表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数。
     */
    @Pointcut("execution(public * com.morningcx.ms.blog.controller.admin..*.*(..)))")
    public void pointCut() {
    }

    @Before(value = "pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        // 如果没有免登录注解，则需要登录认证
        if (!sign.getMethod().isAnnotationPresent(FreeAuth.class) &&
                !sign.getDeclaringType().isAnnotationPresent(FreeAuth.class)) {
            ContextUtil.getLoginId();
        }
    }
}

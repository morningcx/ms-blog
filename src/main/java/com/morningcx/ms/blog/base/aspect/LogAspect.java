package com.morningcx.ms.blog.base.aspect;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.mapper.LogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author gcx
 * @date 2019/3/9
 */
@Aspect
@Component
@Order(2)
public class LogAspect {

    @Autowired
    private LogMapper logMapper;

    @Pointcut("execution(public * com.morningcx.ms.blog.controller..*.*(..)))")
    public void pointCut() {
    }

    @Around(value = "pointCut() && @annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = ContextUtil.getAttributes();
        HttpServletRequest request = attributes.getRequest();
        MethodSignature method = (MethodSignature) joinPoint.getSignature();
        Class<?> type = method.getDeclaringType();

        com.morningcx.ms.blog.entity.Log log = new com.morningcx.ms.blog.entity.Log();
        log.setTime(new Date());
        // ip TODO 后续可能需要改进ip获取方式
        log.setIp(request.getRemoteAddr());
        log.setAgent(request.getHeader("user-agent"));
        log.setUrl(request.getRequestURL().toString());
        RequestMapping module = type.getAnnotation(RequestMapping.class);
        log.setModule(module == null ? "" : module.name());
        log.setType(logAnnotation.type().getName());
        log.setContent(logAnnotation.desc());
        log.setMethod(type.getName() + "." + method.getName());
        log.setSession(attributes.getSessionId());
        // 执行方法，返回obj
        Object obj = joinPoint.proceed();
        log.setCost(System.currentTimeMillis() - log.getTime().getTime());
        logMapper.insert(log);
        return obj;
    }
/*
    *//**
     * 解析日志描述
     * @param s
     * @return
     *//*
    private static String parseDesc(String s) {
        StringBuilder sb = new StringBuilder();
        char open = '{', close = '}';
        for (int i = 0, start = 0; i < s.length(); ++i) {
            if (open == s.charAt(i)) {
                int openIndex = i;
                sb.append(s, start, openIndex);
                while (s.charAt(++i) != close) ;
                // arg为{}中的内容，当前i为close的索引
                String arg = s.substring(openIndex + 1, i);
                sb.append(getRealValue(arg));
                start = i + 1;
            }
        }
        return sb.toString();
    }*/


}

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

    @Around(value = "pointCut() && @annotation(log)")
    public Object around(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = ContextUtil.getAttributes();
        HttpServletRequest request = attributes.getRequest();

        MethodSignature method = (MethodSignature) joinPoint.getSignature();
        Class<?> type = method.getDeclaringType();

        com.morningcx.ms.blog.entity.Log logEntity = new com.morningcx.ms.blog.entity.Log();
        // time 第一个设置，after需要计算两者之差
        logEntity.setTime(new Date());
        // ip 后续可能需要改进ip获取方式
        logEntity.setIp(request.getRemoteAddr());
        // agent
        logEntity.setAgent(request.getHeader("user-agent"));
        // url
        logEntity.setUrl(request.getRequestURL().toString());
        // module
        RequestMapping module = type.getAnnotation(RequestMapping.class);
        logEntity.setModule(module == null ? "" : module.name());
        // type
        logEntity.setType(log.type());
        // method
        logEntity.setMethod(type.getName() + "." + method.getName());
        // session id
        logEntity.setSession(attributes.getSessionId());
        // 执行方法，返回obj
        Object obj = joinPoint.proceed();
        // content
        logEntity.setContent(log.desc());
        // cost 不算插入日志的时间
        logEntity.setCost(System.currentTimeMillis() - logEntity.getTime().getTime());
        logMapper.insert(logEntity);
        return obj;
    }
}

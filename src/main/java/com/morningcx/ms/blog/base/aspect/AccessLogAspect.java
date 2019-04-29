package com.morningcx.ms.blog.base.aspect;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.base.util.IpUtil;
import com.morningcx.ms.blog.base.util.LogUtil;
import com.morningcx.ms.blog.entity.AccessLog;
import com.morningcx.ms.blog.mapper.AccessLogMapper;
import com.morningcx.ms.blog.mapper.ConfigMapper;
import eu.bitwalker.useragentutils.UserAgent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author gcx
 * @date 2019/4/29
 */
@Aspect
@Component
public class AccessLogAspect {
    @Autowired
    private AccessLogMapper accessLogMapper;
    @Autowired
    private ConfigMapper configMapper;

    @Pointcut("execution(public * com.morningcx.ms.blog.controller.web..*.*(..)))")
    public void pointCut() {
    }

    @Around(value = "pointCut() && @annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        AccessLog accessLog = new AccessLog();
        // 开始执行时间
        accessLog.setTime(new Date());

        // 可能会发生异常，所以日志处理放在proceed后面
        Object obj = joinPoint.proceed();

        // 处理请求信息
        ServletRequestAttributes attributes = ContextUtil.getAttributes();
        HttpServletRequest request = attributes.getRequest();
        MethodSignature method = (MethodSignature) joinPoint.getSignature();
        Class<?> type = method.getDeclaringType();

        // 获取真实ip
        accessLog.setIp(IpUtil.getRealIp(request));
        // 解析ip地理位置 todo redis
        String ipRegion = IpUtil.ip2region(accessLog.getIp(), configMapper.getValue("ip2regionDbPath"));
        String empty = "0";
        String[] args = ipRegion.split("\\|");
        accessLog.setCountry(empty.equals(args[0]) ? "" : args[0]);
        accessLog.setProvince(empty.equals(args[2]) ? "" : args[2]);
        accessLog.setCity(empty.equals(args[3]) ? "" : args[3]);
        accessLog.setIsp(empty.equals(args[4]) ? "" : args[4]);
        // agent
        accessLog.setAgent(request.getHeader("user-agent"));
        // agent解析
        UserAgent userAgent = UserAgent.parseUserAgentString(accessLog.getAgent());
        accessLog.setOs(userAgent.getOperatingSystem().getGroup().getName());
        accessLog.setBrowser(userAgent.getBrowser().getGroup().getName());
        accessLog.setDevice(userAgent.getOperatingSystem().getDeviceType().getName());
        // 请求url
        accessLog.setUrl(request.getRequestURL().toString());
        // 操作模块
        RequestMapping module = type.getAnnotation(RequestMapping.class);
        accessLog.setModule(module == null ? "" : module.name());
        // 操作类型
        accessLog.setType(logAnnotation.type().getName());
        // 解析操作描述信息
        accessLog.setContent(LogUtil.parseDesc(logAnnotation.desc(), method.getParameterNames(), joinPoint.getArgs()));
        // 执行的方法
        accessLog.setMethod(type.getName() + "." + method.getName());
        // 总耗时
        accessLog.setCost(System.currentTimeMillis() - accessLog.getTime().getTime());
        accessLogMapper.insert(accessLog);
        return obj;
    }

}

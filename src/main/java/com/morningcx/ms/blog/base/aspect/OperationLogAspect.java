package com.morningcx.ms.blog.base.aspect;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.util.ConfigUtil;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.base.util.IpUtil;
import com.morningcx.ms.blog.base.util.LogUtil;
import com.morningcx.ms.blog.entity.OperationLog;
import com.morningcx.ms.blog.mapper.OperationLogMapper;
import eu.bitwalker.useragentutils.UserAgent;
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
public class OperationLogAspect {

    @Autowired
    private OperationLogMapper operationLogMapper;
    @Autowired
    private ConfigUtil configUtil;

    @Pointcut("execution(public * com.morningcx.ms.blog.controller.admin..*.*(..)))")
    public void pointCut() {
    }

    @Around(value = "pointCut() && @annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        Object obj = null;
        Exception exception = null;
        OperationLog operationLog = new OperationLog();
        // 开始执行时间
        operationLog.setTime(new Date());

        try {
            obj = joinPoint.proceed();
        } catch (Exception e) {
            exception = e;
        }

        // 处理请求信息
        ServletRequestAttributes attributes = ContextUtil.getAttributes();
        HttpServletRequest request = attributes.getRequest();
        MethodSignature method = (MethodSignature) joinPoint.getSignature();
        Class<?> type = method.getDeclaringType();

        // 操作人id todo 密码错误插入操作日志就会报错
        operationLog.setUserId(ContextUtil.getLoginId());
        // 获取真实ip
        operationLog.setIp(IpUtil.getRealIp(request));
        // 解析ip地理位置
        String ipRegion = IpUtil.ip2region(operationLog.getIp(), configUtil.getConfig("ip2regionDbPath"));
        String empty = "0";
        String[] args = ipRegion.split("\\|");
        operationLog.setCountry(empty.equals(args[0]) ? "" : args[0]);
        operationLog.setProvince(empty.equals(args[2]) ? "" : args[2]);
        operationLog.setCity(empty.equals(args[3]) ? "" : args[3]);
        operationLog.setIsp(empty.equals(args[4]) ? "" : args[4]);
        // agent
        operationLog.setAgent(request.getHeader("user-agent"));
        // agent解析
        UserAgent userAgent = UserAgent.parseUserAgentString(operationLog.getAgent());
        operationLog.setOs(userAgent.getOperatingSystem().getGroup().getName());
        operationLog.setBrowser(userAgent.getBrowser().getGroup().getName());
        operationLog.setDevice(userAgent.getOperatingSystem().getDeviceType().getName());
        // 请求url
        operationLog.setUrl(request.getRequestURL().toString());
        // 操作模块
        RequestMapping module = type.getAnnotation(RequestMapping.class);
        operationLog.setModule(module == null ? "" : module.name());
        // 操作类型
        operationLog.setType(logAnnotation.type().getName());
        // 解析操作描述信息
        String content = (exception != null ? "error[" + exception.getMessage() + "]-" : "") +
                LogUtil.parseDesc(logAnnotation.desc(), method.getParameterNames(), joinPoint.getArgs());
        operationLog.setContent(content);
        // 执行的方法
        operationLog.setMethod(type.getName() + "." + method.getName());
        // session id
        operationLog.setSession(attributes.getSessionId());
        // 总耗时
        operationLog.setCost(System.currentTimeMillis() - operationLog.getTime().getTime());
        operationLogMapper.insert(operationLog);
        // 若发生异常则抛出
        if (exception != null) {
            throw exception;
        }
        return obj;
    }
}

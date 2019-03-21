package com.morningcx.ms.blog.base.aspect;

import com.morningcx.ms.blog.base.annotation.Log;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.base.util.IpUtil;
import com.morningcx.ms.blog.mapper.LogMapper;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
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
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        com.morningcx.ms.blog.entity.Log log = new com.morningcx.ms.blog.entity.Log();
        // 开始执行时间
        log.setTime(new Date());

        // 可能会发生异常，所以日志处理放在proceed后面
        Object obj = joinPoint.proceed();

        // 处理请求信息
        ServletRequestAttributes attributes = ContextUtil.getAttributes();
        HttpServletRequest request = attributes.getRequest();
        MethodSignature method = (MethodSignature) joinPoint.getSignature();
        Class<?> type = method.getDeclaringType();

        // 操作人id
        log.setUserId(ContextUtil.getLoginId());
        // 获取真实ip
        log.setIp(IpUtil.getRealIp(request));
        // 解析ip地理位置
        String ipRegion = IpUtil.ip2region(log.getIp());
        if ("".equals(ipRegion)) {
            log.setCountry(ipRegion);
            log.setProvince(ipRegion);
            log.setCity(ipRegion);
            log.setIsp(ipRegion);
        } else {
            String empty = "0";
            String[] args = ipRegion.split("\\|");
            log.setCountry(empty.equals(args[0]) ? "" : args[0]);
            log.setProvince(empty.equals(args[2]) ? "" : args[2]);
            log.setCity(empty.equals(args[3]) ? "" : args[3]);
            log.setIsp(empty.equals(args[4]) ? "" : args[4]);
        }
        // agent
        log.setAgent(request.getHeader("user-agent"));
        UserAgent userAgent = UserAgent.parseUserAgentString(log.getAgent());
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();
        log.setOs(operatingSystem.getName() + " " + operatingSystem.getDeviceType().getName());
        log.setBrowser(browser.getName() + " " + userAgent.getBrowserVersion().getVersion() + " " + browser.getBrowserType().getName());
        // 请求url
        log.setUrl(request.getRequestURL().toString());
        // 操作模块
        RequestMapping module = type.getAnnotation(RequestMapping.class);
        log.setModule(module == null ? "" : module.name());
        // 操作类型
        log.setType(logAnnotation.type().getName());
        // 解析操作描述信息
        log.setContent(parseDesc(logAnnotation.desc(), method.getParameterNames(), joinPoint.getArgs()));
        // 执行的方法
        log.setMethod(type.getName() + "." + method.getName());
        // session id
        log.setSession(attributes.getSessionId());
        // 总耗时
        log.setCost(System.currentTimeMillis() - log.getTime().getTime());
        logMapper.insert(log);
        return obj;
    }

    /**
     * 解析日志描述
     *
     * @param s
     * @return
     */
    private String parseDesc(String s, String[] names, Object[] args) throws Exception {
        int min = Math.min(names.length, args.length);
        Map<String, Object> map = new HashMap<>(min);
        for (int i = 0; i < min; ++i) {
            map.put(names[i], args[i]);
        }
        StringBuilder sb = new StringBuilder();
        char open = '{', close = '}';
        int start = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (open == s.charAt(i)) {
                int openIndex = i;
                sb.append(s, start, openIndex);
                while (s.charAt(++i) != close) ;
                // arg为{}中的内容，当前i为close的索引
                String arg = s.substring(openIndex + 1, i);
                sb.append(getRealValue(arg, map));
                start = i + 1;
            }
        }
        return sb.append(s, start, s.length()).toString();
    }

    private String getRealValue(String s, Map<String, Object> map) throws Exception {
        if (s.contains(".")) {
            String[] args = s.split("\\.");
            Object obj = map.get(args[0]);
            Field field = obj.getClass().getDeclaredField(args[1]);
            field.setAccessible(true);
            return field.get(obj).toString();
        }
        Object o = map.get(s);
        return o == null ? "" : o.toString();
    }

}

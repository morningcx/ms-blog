package com.morningcx.ms.blog.base.exception;

import com.morningcx.ms.blog.base.BasicResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * RestControllerAdvice中annotations指向所有带有注解@RestController的控制器
 * 也就是只有添加了@RestController注解的控制器才会进入全局异常处理
 *
 * @author guochenxiao
 * @date 2019/2/18
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 异常处理
     * ResponseStatus 默认返回status为500
     */
    @ExceptionHandler
    @ResponseStatus
    public BasicResult exceptionHandler(Exception e) {
        return new BasicResult(e);
    }
}

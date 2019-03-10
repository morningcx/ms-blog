package com.morningcx.ms.blog.base.exception;

import com.morningcx.ms.blog.base.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;


/**
 * 全局异常处理器
 * RestControllerAdvice中annotations指向所有带有注解@RestController的控制器
 * 也就是只有添加了@RestController注解的控制器才会进入全局异常处理
 *
 * @author gcx
 * @date 2019/2/18
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Valid 注解所抛出的字段验证错误 BAD_REQUEST
     *
     * @param e
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleBindException(Exception e) {
        e.printStackTrace();
        BindingResult bindingResult;
        // valid验证集合中的元素时可能会重复报错，需要裁剪
        Set<String> set = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        // json抛出的异常和表单抛出的异常不一样
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else {
            bindingResult = ((BindException) e).getBindingResult();
        }
        bindingResult.getAllErrors().forEach(error -> {
            if (set.add(error.getDefaultMessage())) {
                sb.append(error.getDefaultMessage()).append("<br>");
            }
        });
        return Result.fail(sb.toString());
    }

    /**
     * 其他错误 INTERNAL_SERVER_ERROR
     * ResponseStatus 默认返回的status就是INTERNAL_SERVER_ERROR
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exceptionHandler(Exception e) {
        e.printStackTrace();
        return Result.fail(e.getMessage());
    }

}

package org.erxi.common.config.exception;

import org.erxi.common.result.Result;
import org.erxi.common.result.ResultCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail().message("Global Exception Handle...");
    }

    // 特定异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        return Result.fail().message("Special Exception Handle...");
    }

    // 自定义异常处理
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e) {
        e.printStackTrace();
        return Result.fail().message(e.getMsg()).code(e.getCode());
    }

    /**
     * spring security 异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.fail().code(205).message("没有操作权限");
    }
}

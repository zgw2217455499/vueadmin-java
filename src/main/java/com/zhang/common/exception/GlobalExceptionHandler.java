package com.zhang.common.exception;


import com.zhang.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e){
        log.info("运行时异常----------{}",e.getMessage());
        return Result.fail(e.getMessage());
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e){
        log.info("Assess异常----------{}",e.getMessage());
        return Result.fail(e.getMessage());
    }
    @ExceptionHandler(value = ArithmeticException.class)
    public Result handler(ArithmeticException e){
        log.info("Access异常---------{}",e.getMessage());
        return Result.fail(e.getMessage());
    }
}

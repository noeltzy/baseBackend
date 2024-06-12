package com.tzy.basebackend.handler;

import com.tzy.basebackend.common.BusinessException;
import com.tzy.basebackend.common.ErrorCode;
import com.tzy.basebackend.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Noel
 * Created on 2024/6/10
 * ClassName:GlobalExceptionHandler
 * Package:com.tzy.basebackend.handler
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result businessExceptionHandler(BusinessException e) {
        log.error("BusinessException:{}", e.getMessage());
        return Result.failure(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException: ", e);
        return Result.failure(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}

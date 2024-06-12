package com.tzy.basebackend.common;

import lombok.Getter;

/**
 * @author Noel
 * Created on 2024/6/10
 * ClassName:BusinessException
 * Package:com.tzy.basebackend.common
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static void error(ErrorCode errorCode) {
        throw new BusinessException(errorCode.getCode(), errorCode.getMessage());
    }

    public static void error(String message) {
        throw new BusinessException(ErrorCode.LOGIC_ERROR.getCode(), message);
    }
}

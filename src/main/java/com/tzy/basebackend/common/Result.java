package com.tzy.basebackend.common;

import lombok.Data;

/**
 * @author Noel
 * Created on 2024/6/10
 * ClassName:Result
 * Package:com.tzy.basebackend.common
 */


@Data
public class Result<T> {
    private int code;
    private T data;
    private String message;
    private String description;

    public Result(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public Result(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Result(ErrorCode code, T data) {
        this.code = code.getCode();
        this.data = data;
        this.message = code.getMessage();
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(ErrorCode.OK, data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(ErrorCode.OK, null);
    }

    public static <T> Result<T> failure(BusinessException e) {
        return new Result<>(e.getCode(), null, e.getMessage());
    }

    public static <T> Result<T> failure(ErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), null, message);
    }

}

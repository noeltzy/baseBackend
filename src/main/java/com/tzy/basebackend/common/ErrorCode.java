package com.tzy.basebackend.common;

/**
 * @author Noel
 * Created on 2024/6/10
 * ClassName:ErrorCode
 * Package:com.tzy.basebackend.common
 */
public enum ErrorCode {
    OK(0, "ok", ""),
    REQUEST_PARAMS_ERROR(40000, "请求参数格式错误", ""),
    LOGIC_ERROR(40000, "请求参数格式错误", ""),
    REQUEST_NULL_ERROR(40001, "请求参数为空", ""),
    NO_LOGIN(40002, "未登录", ""),
    NO_AUTH(40100, "无权限", ""),
    SYSTEM_ERROR(50001, "系统出错", ""),
    ;

    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}

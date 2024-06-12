package com.tzy.basebackend.model.request;

import lombok.Data;

/**
 * @author Noel
 * Created on 2024/6/10
 * ClassName:LoginRequest
 * Package:com.tzy.basebackend.model.request
 */
@Data
public class LoginRequest {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 登录方式
     * 0 : 手机验证码
     * 2 : 账号密码
     */
    private int method = 0;
}


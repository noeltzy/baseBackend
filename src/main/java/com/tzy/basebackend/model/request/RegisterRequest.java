package com.tzy.basebackend.model.request;

import lombok.Data;

/**
 * @author Noel
 * Created on 2024/6/10
 * ClassName:RegisterRequest
 * Package:com.tzy.basebackend.model.request
 */

@Data
public class RegisterRequest {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码
     */
    private String checkPassword;
}

package com.tzy.basebackend.model.DTO;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 性别0男1女
     */
    private Integer gender;
    /**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 手机号
     */
    private String phone;
}
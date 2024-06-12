package com.tzy.basebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tzy.basebackend.common.Result;
import com.tzy.basebackend.model.domain.User;
import com.tzy.basebackend.model.request.LoginRequest;
import com.tzy.basebackend.model.request.RegisterRequest;

/**
 * @author Windows11
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-06-10 19:47:04
 */
public interface UserService extends IService<User> {

    Result<String> loginByCode(LoginRequest loginRequest);

    Result<Long> register(RegisterRequest registerRequest);

    Result<String> sendCode(String phone);

    Result<String> loginByPassword(LoginRequest loginRequest);
}

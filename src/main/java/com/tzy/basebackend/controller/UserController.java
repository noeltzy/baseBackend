package com.tzy.basebackend.controller;

import com.tzy.basebackend.common.BusinessException;
import com.tzy.basebackend.common.ErrorCode;
import com.tzy.basebackend.common.Result;
import com.tzy.basebackend.model.request.LoginRequest;
import com.tzy.basebackend.model.request.RegisterRequest;
import com.tzy.basebackend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author Noel
 * @Created on 2024/6/10
 * @ClassName:UserController
 * @Package:com.tzy.basebackend.controller 用户登录接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/code")
    public Result<String> code(String phone) {
        return userService.sendCode(phone);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest == null) {
            BusinessException.error(ErrorCode.REQUEST_NULL_ERROR);
        }
        int method = loginRequest.getMethod();
        return method == 0 ? userService.loginByCode(loginRequest)
                : userService.loginByPassword(loginRequest);
    }

    @PostMapping("/register")
    public Result<Long> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest == null) {
            BusinessException.error(ErrorCode.REQUEST_NULL_ERROR);
        }
        return userService.register(registerRequest);
    }

}

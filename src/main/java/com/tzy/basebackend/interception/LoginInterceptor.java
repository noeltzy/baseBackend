package com.tzy.basebackend.interception;

import com.tzy.basebackend.common.BusinessException;
import com.tzy.basebackend.common.ErrorCode;
import com.tzy.basebackend.holder.UserHolder;
import com.tzy.basebackend.model.DTO.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Noel
 * Created on 2024/6/13
 * ClassName:LoginInteceptor
 * Package:com.tzy.basebackend.interception
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDTO userDTO = UserHolder.getUserDTO();
        if (userDTO == null) {
            BusinessException.error(ErrorCode.NO_LOGIN);
        }
        return true;
    }
}

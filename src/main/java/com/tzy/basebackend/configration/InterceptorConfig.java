package com.tzy.basebackend.configration;

import com.tzy.basebackend.interception.LogInterceptor;
import com.tzy.basebackend.interception.LoginInterceptor;
import com.tzy.basebackend.interception.RefreshLoginInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * @author Noel
 * Created on 2024/6/13
 * ClassName:InterceptorConfig
 * Package:com.tzy.basebackend.configration
 */

@Configuration
public class InterceptorConfig extends WebMvcConfig {
    /**
     * 日志拦截器
     */
    @Resource
    LogInterceptor logInterceptor;
    /**
     * 登录拦截器
     */
    @Resource
    LoginInterceptor loginInterceptor;

    @Resource
    RefreshLoginInterceptor refreshLoginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(refreshLoginInterceptor).addPathPatterns("/**").order(0);
        registry.addInterceptor(logInterceptor).addPathPatterns("/**").order(1);
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(
                "/user/login",
                "/user/code",
                "/register"
        ).order(2);
    }
}

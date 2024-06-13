package com.tzy.basebackend.configration;

import com.tzy.basebackend.interception.LogInterceptor;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    }
}

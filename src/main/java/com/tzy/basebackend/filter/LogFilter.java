package com.tzy.basebackend.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Noel
 * Created on 2024/6/13
 * ClassName:DemoFilter
 * Package:com.tzy.basebackend.filter
 */
@Slf4j
//@WebFilter(urlPatterns = "/*")
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        log.info("DemoFilter init only can be called once");
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
//        log.info("DemoFilter destroy called only once");
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 放行前的逻辑
        if (servletRequest instanceof HttpServletRequest httpRequest) {
            String requestUrl = httpRequest.getRequestURI();
            String token = httpRequest.getHeader("authorization");
            log.info("访问的URL: {}", requestUrl);
            log.info("token: {}", token);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

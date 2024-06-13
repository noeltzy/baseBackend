package com.tzy.basebackend.interception;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.tzy.basebackend.holder.UserHolder;
import com.tzy.basebackend.model.DTO.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.tzy.basebackend.constant.RedisKey.LOGIN_USER_KEY_PREFIX;
import static com.tzy.basebackend.constant.RedisKey.LOGIN_USER_TTL;

/**
 * @author Noel
 * Created on 2024/6/13
 * ClassName:RefreshLoginInteceptor
 * Package:com.tzy.basebackend.interception
 */
@Component
public class RefreshLoginInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    public RefreshLoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("authorization");
        // 未登录放行
        if (StringUtils.isBlank(token)) {
            return true;
        }
        // Redis查询当前登录用户
        String key = LOGIN_USER_KEY_PREFIX + token;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
        if (CollectionUtil.isEmpty(entries)) {
            return true;
        }
        // 取出userDTO
        UserDTO userDTO = BeanUtil.fillBeanWithMap(entries, new UserDTO(), false);
        // 刷新 登录凭证时间
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        //存储到localThread
        UserHolder.save(userDTO);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserHolder.remove();
    }
}

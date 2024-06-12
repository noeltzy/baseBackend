package com.tzy.basebackend.constant;

/**
 * @author Noel
 * Created on 2024/6/11
 * ClassName:RedisKey
 * Package:com.tzy.basebackend.constant
 */
public interface RedisKey {
    /**
     * 验证码Redis Key &&TTL
     */
    String CODE_KEY_PREFIX = "LOGIN:CODE:";
    Long CODE_TTL = 30L;


    /**
     * 登录有效期 Redis Key &&TTL
     * 30 MIN
     */
    String LOGIN_USER_KEY_PREFIX = "LOGIN:USER:";
    Long LOGIN_USER_TTL = 1800L;
}

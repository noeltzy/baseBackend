package com.tzy.basebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzy.basebackend.common.BusinessException;
import com.tzy.basebackend.common.ErrorCode;
import com.tzy.basebackend.common.Result;
import com.tzy.basebackend.mapper.UserMapper;
import com.tzy.basebackend.model.DTO.UserDTO;
import com.tzy.basebackend.model.domain.User;
import com.tzy.basebackend.model.request.LoginRequest;
import com.tzy.basebackend.model.request.RegisterRequest;
import com.tzy.basebackend.service.UserService;
import com.tzy.basebackend.utils.ValidateUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.tzy.basebackend.constant.RedisKey.*;
import static com.tzy.basebackend.constant.UserConstant.PASSWORD_SALT;

/**
 * @author Windows11
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-06-10 19:47:04
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<String> loginByCode(LoginRequest loginRequest) {
        // 手机号码登录
        String phone = loginRequest.getPhone();
        String code = loginRequest.getCode();
        if (StringUtils.isAnyBlank(phone, code)) {
            BusinessException.error(ErrorCode.REQUEST_NULL_ERROR);
        }

        String cacheCode = stringRedisTemplate.opsForValue().get(CODE_KEY_PREFIX + phone);
        // 验证码不相等
        if (!ObjectUtil.equals(cacheCode, code)) {
            BusinessException.error("验证码错误");
        }
        //验证码相等 查询用户是否存在,不存在则创建用户
        User user = this.query().eq("phone", phone).one();
        // 不存在用户
        if (user == null) {
            //创建用户
            user = this.createUserByPhone(phone);
        }
        //生成TOKEN
        String token = getToken(user);
        return Result.ok(token);
    }

    /**
     * 密码登录
     *
     * @param loginRequest 请求体包含账号密码
     * @return 登录凭证Token
     */

    @Override
    public Result<String> loginByPassword(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            BusinessException.error(ErrorCode.REQUEST_NULL_ERROR);
        }
        String encryptPassword = getEncryptPassword(password);
        User user = query().eq("username", username).eq("password", encryptPassword).one();
        if (user == null) {
            BusinessException.error("账号密码错误");
        }
        String token = getToken(user);
        return Result.ok(token);

    }


    /**
     * 用账号密码时注册方式
     *
     * @param registerRequest 注册提供的参数
     * @return 用户id
     */
    @Override
    public Result<Long> register(RegisterRequest registerRequest) {
        //基础校验
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String checkPassword = registerRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            BusinessException.error(ErrorCode.REQUEST_NULL_ERROR);
        }
        if (!ObjectUtil.equals(password, checkPassword)) {
            BusinessException.error("两次密码不符合");
        }
        Long count = this.query().eq("username", username).count();
        if (count > 0) {
            BusinessException.error("用户名不允许重复");
        }
        // 密码加密
        String encryptPassword = this.getEncryptPassword(password);
        //注册用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPassword);
        boolean save = this.save(user);
        if (!save) {
            BusinessException.error(ErrorCode.SYSTEM_ERROR);
        }
        return Result.ok(user.getId());
    }

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @return 结果
     */
    @Override
    public Result<String> sendCode(String phone) {
        // 校验格式|同时判空
        if (!ValidateUtils.isValidPhoneNumber(phone)) {
            BusinessException.error("手机号格式出错");
        }
        //获取6位验证码
        String code = RandomUtil.randomNumbers(6);
        log.info("code: {}", code);
        String key = CODE_KEY_PREFIX + phone;
        long ttl = CODE_TTL;
        stringRedisTemplate.opsForValue().set(key, code, ttl, TimeUnit.SECONDS);
        return Result.ok();
    }

    /**
     * 密码 加密
     *
     * @param password 密码
     * @return 加密后密码
     */
    private String getEncryptPassword(String password) {
        return DigestUtil.md5Hex(password + PASSWORD_SALT);
    }

    /**
     * 手机创建用户 如果用户不存在
     *
     * @param phone 手机号
     * @return 新用户
     */
    private User createUserByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setUsername(phone + RandomUtil.randomString(3));
        //默认密码手机号
        String password = getEncryptPassword(phone);
        user.setPassword(password);
        this.save(user);
        return user;
    }

    /**
     * 用户登录生成Token并将用户信息 缓存至Redis
     *
     * @param user 用户
     * @return token
     */
    private String getToken(User user) {
        String token = UUID.randomUUID().toString();
        // 缓存用户
        String key = LOGIN_USER_KEY_PREFIX + token;
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions
                        .create()
                        .ignoreNullValue()
                        .setFieldValueEditor((name, value) -> value == null ? null : value.toString())
        );
        // 插入缓存
        stringRedisTemplate.opsForHash().putAll(key, userMap);
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return token;
    }
}





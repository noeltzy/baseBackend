package com.tzy.basebackend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Noel
 * Created on 2024/6/11
 * ClassName:ValidateUtils
 * Package:com.tzy.basebackend.utils
 */
public class ValidateUtils {
    // 定义手机号码的正则表达式，包含可选的国际区号
    private static final String PHONE_NUMBER_PATTERN = "^(\\+\\d{1,3})?1[3-9]\\d{9}$";

    // 创建Pattern对象
    private static final Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);

    /**
     * 校验手机号码是否有效
     *
     * @param phoneNumber 待校验的手机号码
     * @return 如果手机号码有效，返回true；否则，返回false
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}

package com.tzy.basebackend.service;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Noel
 * Created on 2024/6/10
 * ClassName:OtherTest
 * Package:com.tzy.basebackend.service
 */
public class OtherTest {
    @Test
    public void test() {
        String s = RandomUtil.randomNumbers(6);
        System.out.println(s);
    }
}

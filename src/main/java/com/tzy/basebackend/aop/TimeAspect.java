package com.tzy.basebackend.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Noel
 * Created on 2024/6/13
 * ClassName:TimeAspact
 * Package:com.tzy.basebackend.aop
 */

@Component
@Aspect
@Slf4j
public class TimeAspect {
    @Pointcut("execution(* com.tzy.basebackend.service.*.*(..))")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object res = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("{} 方法耗时 {} ms", joinPoint.getSignature(), endTime - startTime);
        return res;
    }
}




package com.demo.springbootdemo.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 日誌記錄切面 (Logging Aspect)
 * 示範 Day 11-12: 使用 AOP (正向切面程式設計) 紀錄系統行為
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * 定義切入點 (Pointcut)
     * 鎖定 library.service 封裝下的所有方法
     */
    @Pointcut("execution(* com.demo.springbootdemo.library.service.*.*(..))")
    public void serviceLayer() {}

    /**
     * 前置通知 (Before Advice): 方法執行前觸發
     */
    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(">>> [AOP 日誌] 準備執行 Service 方法: " + methodName);
    }

    /**
     * 返回通知 (After Returning): 方法成功執行並回傳後觸發
     */
    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("<<< [AOP 日誌] Service 方法 " + methodName + " 執行成功。");
    }
}

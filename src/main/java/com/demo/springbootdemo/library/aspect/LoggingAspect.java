package com.demo.springbootdemo.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 【職責】在 library.service 套件方法進出時輸出簡易呼叫軌跡。
 * 【技巧】以 AspectJ {@code @Pointcut}／{@code @Before}／{@code @AfterReturning} 宣告橫切關注點。
 * 【概念】日誌、交易、安全等橫切邏輯用 AOP 抽出，可避免在每個 Service 方法重複樣板碼。
 * 【邊界】不實作業務邏輯、例外轉譯或正式環境結構化日誌管線。
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * 【職責】定義切入點：library.service 套件下所有公開方法。
     * 【技巧】使用 execution 表達式鎖定套件與方法簽章。
     * 【概念】切入點是「在哪裡織入」；通知（advice）是「織入什麼行為」。
     */
    @Pointcut("execution(* com.demo.springbootdemo.library.service.*.*(..))")
    public void serviceLayer() {}

    /**
     * 【職責】方法進入前記錄即將執行的 Service 方法名稱。
     * 【技巧】自 {@link JoinPoint} 取得簽章名稱後輸出。
     * 【概念】Before advice 適合觀測與前置檢查；此處僅示範觀測。
     */
    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(">>> [AOP 日誌] 準備執行 Service 方法: " + methodName);
    }

    /**
     * 【職責】方法正常返回後記錄成功訊息。
     * 【技巧】{@code @AfterReturning} 僅在無例外返回時觸發。
     * 【概念】與 After／AfterThrowing 搭配可區分成功與失敗路徑的觀測。
     */
    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("<<< [AOP 日誌] Service 方法 " + methodName + " 執行成功。");
    }
}

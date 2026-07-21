package com.demo.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 【職責】作為 SpringBootDemo 的啟動入口，組裝容器並啟用快取、Feign 與 JPA 掃描。
 * 【技巧】以 {@code @SpringBootApplication} 指定掃描基底套件，並排除 {@link SecurityAutoConfiguration} 以簡化示範。
 * 【概念】啟動類只負責框架能力開關；商業規則應放在 Service／Controller，避免與生命週期混雜。
 * 【邊界】不處理 HTTP 路由或資料存取；正式環境不應沿用「關閉 Security」的示範設定。
 */
// Disable Security for demo purpose
@EnableCaching
@EnableFeignClients
@EntityScan("com.demo")
@EnableJpaRepositories("com.demo")
@SpringBootApplication(scanBasePackages = "com.demo", exclude = { SecurityAutoConfiguration.class })
public class SpringBootDemoApplication {

    /**
     * 【職責】啟動內嵌容器並載入 Spring 應用上下文。
     * 【技巧】委派 {@link SpringApplication#run} 完成自動設定與元件掃描。
     * 【概念】main 只是進入點；真正的 Bean 組裝由 Spring Boot 自動設定與掃描完成。
     * @param args 命令列參數
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

}

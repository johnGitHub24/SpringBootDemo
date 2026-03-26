package com.demo.springbootdemo.advanced.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

/**
 * Spring Cloud Gateway MVC 配置
 * 展示如何將門戶請求轉發至後端微服務
 */
@Slf4j
@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return route("user_service")
            .route(path("/get-users/**"), http("http://localhost:8080")) // 轉發至本機
            .filter(setPath("/api/ms/users/{segment}")) // 路徑重寫
            .filter((request, next) -> {
                log.info("【Gateway】 收到請求: {} {}", request.method(), request.path());
                return next.handle(request);
            })
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return route("order_service")
            .route(path("/get-orders/**"), http("http://localhost:8080"))
            .filter(setPath("/api/ms/orders/{segment}"))
            .filter((request, next) -> {
                log.info("【Gateway】 收到請求: {} {}", request.method(), request.path());
                return next.handle(request);
            })
            .build();
    }
}

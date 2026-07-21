package com.demo.springbootdemo.advanced.gateway;

import org.springframework.beans.factory.annotation.Value;
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
 * 【職責】設定 Spring Cloud Gateway MVC 路由，將對外閘道路徑轉發至後端模擬微服務。
 * 【技巧】以 Gateway MVC 的 route／path／filter DSL 組裝 {@link RouterFunction}，並記錄請求軌跡。
 * 【概念】閘道集中路由與橫切過濾，可讓後端服務不必各自處理對外路徑轉換。
 * 【邊界】不負責業務規則、認證授權、限流與熔斷；後端位址綁定 {@code gateway.backend-base-url}。
 */
@Slf4j
@Configuration
public class GatewayConfig {

    @Value("${gateway.backend-base-url:http://localhost:8080}")
    private String backendBaseUrl;

    /**
     * 【職責】註冊用戶服務閘道路由：{@code /get-users/{segment}} → {@code /api/ms/users/{segment}}。
     * 【技巧】以 Gateway MVC route／path／setPath 與日誌 filter 組裝 {@link RouterFunction}。
     * 【概念】閘道改寫對外路徑，後端仍可用內部 API 前綴，降低前後端路徑耦合。
     */
    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return route("user_service")
            .route(path("/get-users/{segment}"), http(backendBaseUrl))
            .filter(setPath("/api/ms/users/{segment}"))
            .filter((request, next) -> {
                log.info("【Gateway】 收到請求: {} {}", request.method(), request.path());
                return next.handle(request);
            })
            .build();
    }

    /**
     * 【職責】註冊訂單服務閘道路由：{@code /get-orders/{segment}} → {@code /api/ms/orders/{segment}}。
     * 【技巧】與用戶路由相同的 DSL 模式，僅路徑與後端前綴不同。
     * 【概念】多條路由共用同一後端基底 URL，可在單機模擬多微服務轉發。
     */
    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return route("order_service")
            .route(path("/get-orders/{segment}"), http(backendBaseUrl))
            .filter(setPath("/api/ms/orders/{segment}"))
            .filter((request, next) -> {
                log.info("【Gateway】 收到請求: {} {}", request.method(), request.path());
                return next.handle(request);
            })
            .build();
    }
}

package com.demo.springbootdemo.advanced.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 【職責】啟用 WebSocket／STOMP 簡易 Broker，並定義應用目的地前綴與握手端點。
 * <p>【技巧】實作 {@link WebSocketMessageBrokerConfigurer}，設定 {@code /topic}、{@code /app} 與 SockJS 端點。
 * <p>【概念】STOMP 在 WebSocket 之上提供目的地語意，讓前後端用訂閱／發送模型溝通。
 * <p>【邊界】不負責訊息內容組裝、訂閱授權或連線狀態持久化。
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 【職責】設定 Broker 與應用目的地前綴。
     * <p>【技巧】{@code enableSimpleBroker("/topic")} 與 {@code setApplicationDestinationPrefixes("/app")}。
     * <p>【概念】訂閱前綴與送訊前綴分離，可避免客戶端誤把應用處理當廣播主題。
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 客戶端訂閱的主題前綴
        config.enableSimpleBroker("/topic");
        // 客戶端發送訊息的目的地前綴
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * 【職責】註冊 STOMP 握手端點 {@code /ws-payment}（含 SockJS 降級）。
     * <p>【技巧】{@code addEndpoint(...).withSockJS()} 讓不支援原生 WebSocket 的環境可降級。
     * <p>【概念】握手端點是連線入口；訂閱主題則在 Broker 前綴下另行約定。
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 定義 WebSocket 連接端點
        registry.addEndpoint("/ws-payment").withSockJS();
    }
}

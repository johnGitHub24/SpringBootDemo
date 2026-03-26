package com.demo.springbootdemo.advanced.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 支付通知控制器 (WebSocket/STOMP 實作)
 * 
 * 技術原理：
 * 1. 異步推播：伺服器主動發送訊息給前端，而非前端輪詢。
 * 2. STOMP 協議：在 WebSocket 之上的子協議，定義了主題 (Topic) 訂閱機制。
 * 
 * 快速上手：
 * - 前端訂閱: /topic/payments
 * - 前端發送: /app/notify
 */
@Slf4j
@Controller
public class PaymentNotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public PaymentNotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 技術方法：@MessageMapping
     * 接收來自客戶端的 WebSocket 訊息
     */
    @MessageMapping("/notify")
    @SendTo("/topic/payments")
    public String handleClientMessage(String message) {
        log.info("收到客戶端 WebSocket 訊息: {}", message);
        return "伺服器已接收: " + message;
    }

    /**
     * 技術方法：SimpMessagingTemplate
     * 提供 REST 端點觸發 WebSocket 推播 (用於異步通知場景)
     */
    @GetMapping("/api/test-push")
    @ResponseBody
    public String triggerPush(@RequestParam String msg) {
        log.info("觸發人工推播: {}", msg);
        // 主動推播至訂閱了 /topic/payments 的所有客戶端
        messagingTemplate.convertAndSend("/topic/payments", "【系統通知】 " + msg);
        return "推播請求已發送";
    }
}

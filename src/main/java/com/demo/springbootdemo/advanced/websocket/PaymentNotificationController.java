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
 * 【職責】提供支付通知的 STOMP 與 REST 觸發入口。
 * 【技巧】結合 {@code @MessageMapping}/{@code @SendTo} 與 {@link SimpMessagingTemplate} 主動推播。
 * 【概念】WebSocket 適合伺服器主動通知；REST 觸發端點方便測試推播而不必先寫前端。
 * 【邊界】不負責支付業務規則、持久化或訂閱者身分驗證。
 */
@Slf4j
@Controller
public class PaymentNotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * @param messagingTemplate 用於伺服器主動推播至 STOMP 主題的訊息範本
     */
    public PaymentNotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 接收客戶端 STOMP 訊息並廣播至支付通知主題，讓所有訂閱者同步收到回覆。
     *
     * @param message 客戶端送出的原始字串內容
     * @return 伺服器確認回覆，將由 {@code @SendTo} 推至 {@code /topic/payments}
     */
    @MessageMapping("/notify")
    @SendTo("/topic/payments")
    public String handleClientMessage(String message) {
        log.info("收到客戶端 WebSocket 訊息: {}", message);
        return "伺服器已接收: " + message;
    }

    /**
     * 以 REST 觸發伺服器端推播，便於非 WebSocket 來源（如排程、異步回調）驗證即時通知。
     *
     * @param msg 要推播給訂閱者的通知內容
     * @return 表示推播請求已受理的確認字串
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

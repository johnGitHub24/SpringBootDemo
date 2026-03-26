package com.demo.interview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 消息佇列範例的單元測試
 */
public class MessagingExamplesTest {

    private final MessagingExamples examples = new MessagingExamples();

    /**
     * 測試 RabbitMQ 的 Topic 交換器路由邏輯。
     * 驗證正則模擬是否正確處理 '#' (多詞匹配) 與 '*' (單詞匹配) 的語義。
     */
    @Test
    public void testRabbitMQTopicRoutingLogic() {
        // --- 情況 1: 使用 '#' 進行零個或多個單詞匹配 ---
        String patternSharp = "usa.#";
        assertEquals("訊息匹配成功! 發送到佇列", examples.routeMessageWithTopic("usa.news", patternSharp), "# 應匹配單個單詞");
        assertEquals("訊息匹配成功! 發送到佇列", examples.routeMessageWithTopic("usa.news.weather", patternSharp), "# 應匹配多個單詞");
        assertEquals("訊息匹配成功! 發送到佇列", examples.routeMessageWithTopic("usa", patternSharp), "# 亦可匹配前綴本身");

        // --- 情況 2: 使用 '*' 進行精確一個單詞匹配 ---
        String patternStar = "usa.*";
        assertEquals("訊息匹配成功! 發送到佇列", examples.routeMessageWithTopic("usa.news", patternStar), "* 應匹配精確一個單詞");
        assertEquals("路由鍵不匹配", examples.routeMessageWithTopic("usa.news.weather", patternStar), "* 不應匹配多個單詞");
        
        System.out.println("RabbitMQ Topic 路由與正則模擬驗證通過");
    }
}

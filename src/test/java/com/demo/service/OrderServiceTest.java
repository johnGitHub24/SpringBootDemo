package com.demo.service;

import com.demo.dto.OrderRequest;
import com.demo.kafka.OrderProducer;
import com.demo.model.Order;
import com.demo.model.OrderStatus;
import com.demo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * 【職責】覆蓋 {@link OrderService} 公開行為的單元測試（Mock Repository／Producer）。
 * <p>【技巧】{@code MockitoExtension} 隔離 JPA 與 Kafka，只斷言建立狀態與查無資料契約。
 * <p>【概念】公開 Service ≥1 單元測；與 {@code com.demo.api.OrderApiIntegrationTest} 共用 Case ID。
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private OrderService orderService;

    /**
     * CASE-ORDER-001：建立訂單後為 PENDING 並嘗試發事件。
     * <br>Given: 合法 OrderRequest；When: createOrder；Then: status=PENDING、save／sendOrderEvent 各一次。
     */
    @Test
    void createOrder_persistsPendingAndPublishes() {
        OrderRequest request = new OrderRequest();
        request.setCustomerName("Guest");
        request.setProductName("Test Product");
        request.setQuantity(5);
        request.setPrice(new BigDecimal("99.99"));

        given(orderRepository.save(any(Order.class))).willAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        Order saved = orderService.createOrder(request);

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(saved.getProductName()).isEqualTo("Test Product");
        verify(orderRepository).save(any(Order.class));
        verify(orderProducer).sendOrderEvent(any(Order.class));
    }

    /**
     * CASE-ORDER-002：查無訂單回 empty（對應 HTTP 404）。
     * <br>Given: Repository 無此 id；When: getOrderById(999)；Then: Optional.empty。
     */
    @Test
    void getOrderById_missing_returnsEmpty() {
        given(orderRepository.findById(999L)).willReturn(Optional.empty());

        assertThat(orderService.getOrderById(999L)).isEmpty();
    }
}

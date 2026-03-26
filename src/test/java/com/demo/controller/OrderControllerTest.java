package com.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.model.Order;
import com.demo.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.demo.dto.OrderRequest;
import com.demo.service.OrderService;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.demo.springbootdemo.SpringBootDemoApplication;

@SpringBootTest(classes = SpringBootDemoApplication.class, properties = "seata.enabled=false")
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    public void testCreateAndGetOrder() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setProductName("Test Product");
        request.setQuantity(5);
        request.setPrice(new BigDecimal("99.99"));

        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("Guest");
        order.setStatus(OrderStatus.PENDING);

        given(orderService.createOrder(any(OrderRequest.class))).willReturn(order);
        given(orderService.getAllOrders()).willReturn(java.util.Collections.singletonList(order));

        // Create Order
        String orderJson = objectMapper.writeValueAsString(request);
        
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"));

        // List Orders
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}

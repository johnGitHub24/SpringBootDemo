package com.demo.springbootdemo.advanced.ms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 模擬「用戶微服務」
 */
@RestController
@RequestMapping("/api/ms/users")
public class SimulatedUserController {

    @GetMapping("/{id}")
    public Map<String, Object> getUser(@PathVariable String id) {
        return Map.of(
            "id", id,
            "name", "User_" + id,
            "role", "DEVELOPER",
            "source", "Microservice_User"
        );
    }
}

package com.demo.springbootdemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "orders";
    }

    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }
}

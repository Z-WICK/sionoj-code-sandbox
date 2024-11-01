package com.sion.sionojcodesandbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : wick
 * @Date : 2024/11/1 17:08
 */
@RestController("/")
public class MainController {
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

}

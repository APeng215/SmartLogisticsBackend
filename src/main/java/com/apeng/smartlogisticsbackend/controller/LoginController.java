package com.apeng.smartlogisticsbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仅用于测试 API
 */
@RestController
@RequestMapping("/login")
public class LoginController {


    @GetMapping
    private String loginTest() {
        return "Logged in";
    }


}

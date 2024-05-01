package com.example.modoproject.Pay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentsController {
    @GetMapping("/iamport")
    public String iamport(){
        return "index";
    }
}
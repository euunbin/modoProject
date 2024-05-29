package com.example.modoproject.Pay.controller;

import com.example.modoproject.Pay.entity.Payment;
import com.example.modoproject.Pay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PaymentsController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/iamport")
    public String iamport() {
        return "index";
    }

    @PostMapping("/payment/callback")
    @ResponseBody
    public String paymentCallback(@RequestBody Payment payment) {
        paymentService.savePayment(payment);
        return "Payment saved successfully";
    }
}

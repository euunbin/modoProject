package com.example.modoproject.Pay.controller;

import com.example.modoproject.Pay.entity.Payment;
import com.example.modoproject.Pay.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/iamport")
    public String iamport() {
        return "index";
    }

    @PostMapping("/payment/callback")
    @ResponseBody
    public String paymentCallback(@RequestBody Payment payment) {
        HttpSession session = httpServletRequest.getSession();
        String externalId = (String) session.getAttribute("externalId");
        payment.setExternalId(externalId);

        paymentService.savePayment(payment);
        return "Payment saved successfully";
    }
}

package com.example.modoproject.Pay.controller;

import com.example.modoproject.Pay.entity.Payment;
import com.example.modoproject.Pay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentsController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

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

    @PostMapping("/payment")
    public ResponseEntity<String> processPayment(@RequestBody Payment payment) {
        paymentService.savePayment(payment);
        return ResponseEntity.ok("결제가 성공적으로 완료되었습니다.");
    }

    @GetMapping("/payments/user")
    public List<Payment> getUserPayments() {
        HttpSession session = httpServletRequest.getSession();
        String externalId = (String) session.getAttribute("externalId");

        if (externalId != null) {
            return paymentService.getPaymentsByExternalId(externalId);
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/payments/company")
    public List<Payment> getCompanyPayments() {
        HttpSession session = httpServletRequest.getSession();
        String companyId = (String) session.getAttribute("companyId");

        if (companyId != null) {
            return paymentService.getPaymentsByCompanyId(companyId);
        } else {
            return new ArrayList<>();
        }
    }

}

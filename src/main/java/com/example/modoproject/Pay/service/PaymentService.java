package com.example.modoproject.Pay.service;

import com.example.modoproject.Pay.entity.Payment;
import com.example.modoproject.Pay.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByExternalId(String externalId) {
        return paymentRepository.findByExternalId(externalId);
    }
}

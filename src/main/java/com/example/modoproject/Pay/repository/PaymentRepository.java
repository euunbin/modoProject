package com.example.modoproject.Pay.repository;

import com.example.modoproject.Pay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

package com.example.modoproject.Pay.repository;

import com.example.modoproject.Pay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByExternalId(String externalId);
}

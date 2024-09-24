package com.example.modoproject.Pay.repository;

import com.example.modoproject.Pay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByExternalId(String externalId);
    Optional<Payment> findByMerchantUid(String merchantUid);

    List<Payment> findByCompanyId(String companyId);
}

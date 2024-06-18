package com.example.modoproject.cart.repository;

import com.example.modoproject.cart.entity.CartItenEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CartItemRepository extends JpaRepository<CartItenEntity, Long> {
    CartItenEntity findByMerchanUidAndCompanyId(String merchanUid, Long companyId);
}

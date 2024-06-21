package com.example.modoproject.cart.repository;

import com.example.modoproject.cart.entity.CartItenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItenEntity, Long> {
    // 특정 사용자와 회사의 메뉴 ID로 장바구니 항목 찾기
    CartItenEntity findByMerchanUidAndCompanyIdAndExternalId(String merchanUid, Long companyId, String externalId);
    // 특정 사용자의 모든 장바구니 항목 찾기
    List<CartItenEntity> findByExternalId(String externalId);
}

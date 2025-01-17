package com.example.modoproject.cart.repository;

import com.example.modoproject.cart.entity.CartItenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItenEntity, Long> {
    // 특정 사용자와 회사의 메뉴 ID로 장바구니 항목 찾기
    CartItenEntity findByMerchanUidAndCompanyIdAndExternalId(String merchanUid, String companyId, String externalId);
    // 특정 사용자의 모든 장바구니 항목 찾기
    List<CartItenEntity> findByExternalId(String externalId);

    // 특정 사용자와 회사의 메뉴 ID로 여러 장바구니 항목 찾기 (새로운 메서드 추가)
    List<CartItenEntity> findAllByMerchanUidAndCompanyIdAndExternalId(String merchanUid, String companyId, String externalId);

}

package com.example.modoproject.cart.service;


import com.example.modoproject.cart.entity.CartItenEntity;
import com.example.modoproject.cart.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    // 장바구니에 항목 추가
    public CartItenEntity addToCart(String merchanUid, String companyId, String name, int price, String imageUrl, String externalId) {
        // 같은 메뉴가 이미 장바구니에 있는지 확인
        CartItenEntity existingCartItem = cartItemRepository.findByMerchanUidAndCompanyIdAndExternalId(merchanUid, companyId, externalId);
        if (existingCartItem != null) {
            return existingCartItem;
        } else {
            // 새로운 항목을 추가
            CartItenEntity cartItem = new CartItenEntity();
            cartItem.setMerchanUid(merchanUid);
            cartItem.setCompanyId(companyId);
            cartItem.setName(name);
            cartItem.setPrice(price);
            cartItem.setImageUrl(imageUrl);
            cartItem.setExternalId(externalId);
            return cartItemRepository.save(cartItem);
        }
    }

    // 특정 사용자의 장바구니 항목 목록 조회
    public List<CartItenEntity> getCartItemsByExternalId(String externalId) {
        return cartItemRepository.findByExternalId(externalId);
    }

    // 장바구니에서 항목 삭제
    public void removeFromCart(Long id) {
        cartItemRepository.deleteById(id);
    }

    // 결제된 항목 삭제 로직
    public void deletePaidItems(List<String> merchantUids, String externalId, String companyId) {
        for (String fullMerchanUid : merchantUids) {
            // fullMerchanUid에서 실제 장바구니에 저장된 부분만 추출 (예: q3AdW5Tl44ducPwXPLY3)
            String merchanUid = fullMerchanUid.split("\\.")[1]; // "."을 기준으로 두 번째 부분 추출

            System.out.println("삭제 요청: merchanUid=" + merchanUid + ", companyId=" + companyId + ", externalId=" + externalId);

            List<CartItenEntity> items = cartItemRepository.findAllByMerchanUidAndCompanyIdAndExternalId(merchanUid, companyId, externalId);

            if (items.isEmpty()) {
                System.out.println("삭제할 항목이 없습니다: merchantUid=" + merchanUid + ", companyId=" + companyId);
            } else {
                System.out.println("삭제할 항목: " + items.size() + "개");
                cartItemRepository.deleteAll(items);
            }
        }
    }

}

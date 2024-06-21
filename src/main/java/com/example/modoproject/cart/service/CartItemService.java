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
    public CartItenEntity addToCart(String merchanUid, Long companyId, String name, int price, int count, String imageUrl, String externalId) {
        // 같은 메뉴가 이미 장바구니에 있는지 확인
        CartItenEntity existingCartItem = cartItemRepository.findByMerchanUidAndCompanyIdAndExternalId(merchanUid, companyId, externalId);
        if (existingCartItem != null) {
            // 이미 있으면 수량만 증가
            existingCartItem.setCount(existingCartItem.getCount() + count);
            existingCartItem.setMoneyTotal(existingCartItem.getPrice() * existingCartItem.getCount());
            return cartItemRepository.save(existingCartItem);
        } else {
            // 새로운 항목을 추가
            CartItenEntity cartItem = new CartItenEntity();
            cartItem.setMerchanUid(merchanUid);
            cartItem.setCompanyId(companyId);
            cartItem.setName(name);
            cartItem.setPrice(price);
            cartItem.setCount(count);
            cartItem.setImageUrl(imageUrl);
            cartItem.setMoneyTotal(price * count);
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
}

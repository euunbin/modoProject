package com.example.modoproject.cart.controller;

import com.example.modoproject.cart.entity.CartItenEntity;
import com.example.modoproject.cart.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {
    @Autowired
    private CartItemService cartService;

    // 장바구니에 항목 추가
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam String merchanUid,
                                            @RequestParam String companyId,
                                            @RequestParam String name,
                                            @RequestParam int price,
                                            @RequestParam String imageUrl,
                                            HttpSession session) {
        String externalId = (String) session.getAttribute("externalId");
        cartService.addToCart(merchanUid, companyId, name, price, imageUrl, externalId);
        return ResponseEntity.ok("{\"status\":\"success\"}");
    }

    // 장바구니 항목 보기
    @GetMapping("/view")
    public List<CartItenEntity> viewCart(HttpSession session) {
        // 세션에서 사용자 externalId를 가져옴
        String externalId = (String) session.getAttribute("externalId");
        return cartService.getCartItemsByExternalId(externalId);
    }

    // 장바구니에서 항목 삭제
    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return "{\"status\":\"success\"}";
    }

}

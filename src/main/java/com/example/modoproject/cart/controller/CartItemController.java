package com.example.modoproject.cart.controller;

import com.example.modoproject.cart.entity.CartItenEntity;
import com.example.modoproject.cart.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

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

    // 결제 후 장바구니에서 결제된 항목 삭제
    @PostMapping("/deletePaidItems")
    public ResponseEntity<String> deletePaidItems(@RequestBody Map<String, Object> requestData, HttpSession session) {
        // 세션에서 사용자 externalId를 가져옴
        String externalId = (String) session.getAttribute("externalId");
        try {
            // 요청에서 merchantUids와 companyId를 추출
            List<String> merchantUids = (List<String>) requestData.get("merchantUids");
            String companyId = (String) requestData.get("companyId");

            // 장바구니 항목 삭제 서비스 호출
            cartService.deletePaidItems(merchantUids, externalId, companyId);

            return ResponseEntity.ok("{\"status\":\"success\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }

}

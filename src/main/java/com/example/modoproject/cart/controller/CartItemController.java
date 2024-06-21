package com.example.modoproject.cart.controller;



import com.example.modoproject.cart.entity.CartItenEntity;
import com.example.modoproject.cart.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartItemController {
    @Autowired
    private CartItemService cartService;

    // 장바구니에 항목 추가
    @PostMapping("/add")
    public String addToCart(@RequestParam String merchanUid,
                            @RequestParam Long companyId,
                            @RequestParam String name,
                            @RequestParam int price,
                            @RequestParam int count,
                            @RequestParam String imageUrl,
                            HttpSession session) {
        // 세션에서 사용자 externalId를 가져옴
        String externalId = (String) session.getAttribute("externalId");
        cartService.addToCart(merchanUid, companyId, name, price, count, imageUrl, externalId);
        return "redirect:/cart/view";
    }

    // 장바구니 항목 보기
    @GetMapping("/view")
    public String viewCart(HttpSession session, Model model) {
        // 세션에서 사용자 externalId를 가져옴
        String externalId = (String) session.getAttribute("externalId");
        List<CartItenEntity> cartItems = cartService.getCartItemsByExternalId(externalId);
        model.addAttribute("cartItems", cartItems);
        return "cartview"; // cartview.html 뷰를 반환합니다.
    }

    // 장바구니에서 항목 삭제
    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return "redirect:/cart/view";
    }
}

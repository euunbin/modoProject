package com.example.modoproject.cart.controller;


import com.example.modoproject.cart.service.CartItemService;
import com.example.modoproject.cart.entity.CartItenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartItemController {
    @Autowired
    private CartItemService cartService;

    @PostMapping("/add")
    public String addToCart(@RequestParam String merchanUid,
                            @RequestParam Long companyId,
                            @RequestParam String name,
                            @RequestParam int price,
                            @RequestParam int count,
                            @RequestParam String imageUrl) {
        cartService.addToCart(merchanUid, companyId, name, price, count, imageUrl);
        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String viewCart(Model model) {
        List<CartItenEntity> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);
        return "cartview"; // cartview.html 뷰를 반환합니다.
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return "redirect:/cart/view";
    }
}
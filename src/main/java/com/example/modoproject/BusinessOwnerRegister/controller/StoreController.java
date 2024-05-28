package com.example.modoproject.BusinessOwnerRegister.controller;

import com.example.modoproject.BusinessOwnerRegister.Service.StoreService;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StoreController {
    @Autowired
    private StoreService storeService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("store", new Store());
        return "register";
    }

    @PostMapping("/stores")
    public String registerStore(@ModelAttribute Store store) {
        storeService.registerStore(store);
        return "redirect:/stores";
    }
}

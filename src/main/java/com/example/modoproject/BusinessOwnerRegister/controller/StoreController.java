package com.example.modoproject.BusinessOwnerRegister.controller;

import com.example.modoproject.BusinessOwnerRegister.Service.StoreService;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.util.List;

@Controller
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("store", new Store());
        return "register";
    }
    @GetMapping("/stores")
    public String showStoreList(Model model, HttpSession session) {
        List<Store> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);
        model.addAttribute("externalId", session.getAttribute("externalId"));
        return "storeList";
    }
    @PostMapping("/stores")
    public String registerStore(@ModelAttribute Store store) {
        storeService.registerStore(store);
        return "redirect:/stores";
    }


    @GetMapping("/stores/edit/{companyId}")
    public String showEditForm(@PathVariable String companyId, Model model) {
        Store store = storeService.findByCompanyId(companyId);
        if (store != null) {
            model.addAttribute("store", store);
            return "storeupdate";
        } else {
            return "redirect:/stores"; // 가게를 찾을 수 없는 경우
        }
    }

    @PostMapping("/stores/edit/{companyId}")
    public String updateStore(@PathVariable String companyId, @ModelAttribute Store store) {
        storeService.updateStore(companyId, store);
        return "redirect:/stores";
    }

}


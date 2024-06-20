package com.example.modoproject.BusinessOwnerRegister.controller;

import com.example.modoproject.BusinessOwnerRegister.Service.StoreService;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/stores")
    public String registerStore(@ModelAttribute Store store) {
        storeService.registerStore(store);
        return "redirect:/stores";
    }

    @GetMapping("/stores")
    public String listStores(Model model) {
        List<Store> stores = storeService.findAllStores();
        model.addAttribute("stores", stores);
        return "storeList";
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

    @PostMapping("/favorite/toggle/{companyId}")
    public String toggleFavorite(@PathVariable String companyId) {
        storeService.toggleFavorite(companyId);
        return "redirect:/favoriteStores";
    }

    @GetMapping("/favoriteStores")
    public String listFavoriteStores(Model model) {
        List<Store> favoriteStores = storeService.findFavoriteStores();
        model.addAttribute("stores", favoriteStores);
        return "favoriteStores";
    }
}


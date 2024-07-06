package com.example.modoproject.BusinessOwnerRegister.controller;

import com.example.modoproject.BusinessOwnerDashBoard.entity.Menu;
import com.example.modoproject.BusinessOwnerRegister.Service.StoreService;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import com.example.modoproject.BusinessOwnerDashBoard.repository.MenuRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/stores") // API 엔드포인트를 명확히 하기 위해 추가
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private MenuRepository menuRepository;

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

    // REST API 메서드 추가
    @GetMapping
    @ResponseBody
    public List<Store> getAllStores() {
        return storeService.getAllStores();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        Optional<Store> store = storeService.findById(id);
        return store.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/menu")
    @ResponseBody
    public List<Menu> getMenuByStoreId(@PathVariable Long id) {
        return menuRepository.findByCompanyId(id);
    }
}

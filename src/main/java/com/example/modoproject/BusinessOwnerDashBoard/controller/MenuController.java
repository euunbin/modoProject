package com.example.modoproject.BusinessOwnerDashBoard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import com.example.modoproject.BusinessOwnerDashBoard.entity.Menu;
import com.example.modoproject.BusinessOwnerDashBoard.service.MenuService;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseEntity<List<Menu>> showMenuList() {
        List<Menu> menuList = menuService.getAllMenus();
        return ResponseEntity.ok(menuList);
    }

    @PostMapping("/add")
    public ResponseEntity<Menu> addMenu(@RequestParam("companyId") Long companyId,
                                        @RequestParam("name") String name,
                                        @RequestParam("price") int price,
                                        @RequestParam("image") MultipartFile image) throws IOException {
        Menu menu = menuService.saveMenu(companyId, name, price, image);
        return ResponseEntity.ok(menu);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Menu> updateMenu(@PathVariable Long id,
                                           @RequestParam("companyId") Long companyId,
                                           @RequestParam("name") String name,
                                           @RequestParam("price") int price,
                                           @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        Menu updatedMenu = menuService.updateMenu(id, companyId, name, price, image);
        return ResponseEntity.ok(updatedMenu);
    }

    @PostMapping("/addAll")
    public ResponseEntity<Void> addAllMenus(@RequestParam("companyId") Long companyId,
                                            @RequestParam("names") String[] names,
                                            @RequestParam("prices") String[] prices,
                                            @RequestParam("images") MultipartFile[] images) throws IOException {
        for (int i = 0; i < names.length; i++) {
            MultipartFile image = (images.length > i) ? images[i] : null;
            menuService.saveMenu(companyId, names[i], Integer.parseInt(prices[i]), image);
        }
        return ResponseEntity.noContent().build();
    }
}

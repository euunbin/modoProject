package com.example.modoproject.Favorites.controller;

import com.example.modoproject.BusinessOwnerRegister.Service.StoreService;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import com.example.modoproject.Favorites.entity.Favorites;
import com.example.modoproject.Favorites.service.FavoritesService;
import com.example.modoproject.login.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private HttpSession session;

    @PostMapping
    public ResponseEntity<Void> toggleFavorite(@RequestBody List<String> companyIds) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        String externalId = user.getExternalId();

        for (String id : companyIds) {
            Favorites existingFavorite = favoritesService.getFavoriteByExternalIdAndCompanyId(externalId, id);
            if (existingFavorite != null) {
                favoritesService.removeFavorite(externalId, id);
            } else {
                favoritesService.addFavorite(externalId, id);
            }
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestParam String companyId) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        String externalId = user.getExternalId();
        favoritesService.removeFavorite(externalId, companyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{externalId}")
    public ResponseEntity<List<Favorites>> getFavoritesByUser(@PathVariable String externalId) {
        List<Favorites> favorites = favoritesService.getFavoritesByUser(externalId);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/store/{companyId}")
    public ResponseEntity<List<Favorites>> getFavoritesByStore(@PathVariable String companyId) {
        List<Favorites> favorites = favoritesService.getFavoritesByStore(companyId);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping
    public ResponseEntity<Favorites> getFavoriteByExternalIdAndCompanyId(@RequestParam String externalId, @RequestParam String companyId) {
        Favorites favorite = favoritesService.getFavoriteByExternalIdAndCompanyId(externalId, companyId);
        return ResponseEntity.ok(favorite);
    }

    @GetMapping("/favoriteStores")
    public ResponseEntity<List<Store>> getFavoriteStores(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.ok(List.of());
        }
        String externalId = user.getExternalId();
        List<Favorites> favorites = favoritesService.getFavoritesByUser(externalId);
        List<String> companyIds = favorites.stream()
                .map(Favorites::getCompanyId)
                .collect(Collectors.toList());
        List<Store> stores = storeService.getStoresByCompanyIds(companyIds);
        return ResponseEntity.ok(stores);
    }
}

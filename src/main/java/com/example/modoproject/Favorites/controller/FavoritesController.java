package com.example.modoproject.Favorites.controller;

import com.example.modoproject.Favorites.entity.Favorites;
import com.example.modoproject.Favorites.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @PostMapping
    public ResponseEntity<Favorites> addFavorite(@RequestParam String externalId, @RequestParam String companyId) {
        Favorites favorite = favoritesService.addFavorite(externalId, companyId);
        return ResponseEntity.ok(favorite);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestParam String externalId, @RequestParam String companyId) {
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
}

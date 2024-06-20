package com.example.modoproject.Favorites.service;

import com.example.modoproject.Favorites.entity.Favorites;
import com.example.modoproject.Favorites.repository.FavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritesService {

    @Autowired
    private FavoritesRepository favoritesRepository;

    public Favorites addFavorite(String externalId, String companyId) {
        Favorites favorite = new Favorites(externalId, companyId);
        return favoritesRepository.save(favorite);
    }

    public void removeFavorite(String externalId, String companyId) {
        favoritesRepository.deleteByExternalIdAndCompanyId(externalId, companyId);
    }

    public List<Favorites> getFavoritesByUser(String externalId) {
        return favoritesRepository.findByExternalId(externalId);
    }

    public List<Favorites> getFavoritesByStore(String companyId) {
        return favoritesRepository.findByCompanyId(companyId);
    }

    public Favorites getFavoriteByExternalIdAndCompanyId(String externalId, String companyId) {
        return favoritesRepository.findByExternalIdAndCompanyId(externalId, companyId);
    }
}

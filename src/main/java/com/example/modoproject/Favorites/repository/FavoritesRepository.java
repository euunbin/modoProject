package com.example.modoproject.Favorites.repository;

import com.example.modoproject.Favorites.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    void deleteByExternalIdAndCompanyId(String externalId, String companyId);
    List<Favorites> findByExternalId(String externalId);
    List<Favorites> findByCompanyId(String companyId);
    Favorites findByExternalIdAndCompanyId(String externalId, String companyId);
}

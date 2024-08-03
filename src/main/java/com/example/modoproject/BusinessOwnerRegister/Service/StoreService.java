package com.example.modoproject.BusinessOwnerRegister.Service;

import com.example.modoproject.BusinessOwnerRegister.Repository.StoreRepository;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import com.example.modoproject.Favorites.repository.FavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private FavoritesRepository favoritesRepository;

    public Store registerStore(Store store) {
        store.setRegistrationDate(LocalDateTime.now());
        return storeRepository.save(store);
    }

    public Store findByCompanyId(String companyId) {
        return storeRepository.findByCompanyId(companyId);
    }

    public Store updateStore(String oldCompanyId, Store updatedStore) {
        Store existingStore = storeRepository.findByCompanyId(oldCompanyId);
        if (existingStore != null) {
            // 업데이트할 필드 설정
            existingStore.setName(updatedStore.getName());
            existingStore.setAddress(updatedStore.getAddress());
            existingStore.setPhoneNumber(updatedStore.getPhoneNumber());
            existingStore.setFoodType(updatedStore.getFoodType());
            existingStore.setImageUrl(updatedStore.getImageUrl());
            existingStore.setDescription(updatedStore.getDescription());

            if (updatedStore.getCompanyId() != null && !updatedStore.getCompanyId().equals(oldCompanyId)) {
                Store existingStoreWithNewCompanyId = storeRepository.findByCompanyId(updatedStore.getCompanyId());
                if (existingStoreWithNewCompanyId != null) {
                    throw new IllegalArgumentException("새로운 companyId가 이미 사용 중입니다.");
                }
                existingStore.setCompanyId(updatedStore.getCompanyId());
            }

            return storeRepository.save(existingStore);
        } else {
            return null;
        }
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public List<Store> getStoresByCompanyIds(List<String> companyIds) {
        return storeRepository.findByCompanyIdIn(companyIds);
    }

    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }

    public Store findByExternalId(String externalId) {
        return storeRepository.findByExternalId(externalId);
    }


    public Store getStoreByCompanyId(String companyId) {
        return storeRepository.findByCompanyId(companyId);
    }

    public Store getStoreByExternalId(String externalId) {
        return storeRepository.findByExternalId(externalId);
    }
}

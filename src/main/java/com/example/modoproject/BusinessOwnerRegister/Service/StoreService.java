package com.example.modoproject.BusinessOwnerRegister.Service;

import com.example.modoproject.BusinessOwnerRegister.Repository.StoreRepository;
import com.example.modoproject.BusinessOwnerRegister.Repository.StoreRequestRepository;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import com.example.modoproject.BusinessOwnerRegister.entity.StoreRequest;
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
    private StoreRequestRepository storeRequestRepository;

    @Autowired
    private FavoritesRepository favoritesRepository;

    public Store registerStore(Store store) {
        store.setRegistrationDate(LocalDateTime.now());
        return storeRepository.save(store);
    }

    public Store findByCompanyId(String companyId) {
        return storeRepository.findByCompanyId(companyId);
    }

    public Store updateStore(String companyId, Store updatedStore) {
        Store store = storeRepository.findByCompanyId(companyId);
        if (store != null) {
            store.setName(updatedStore.getName());
            store.setAddress(updatedStore.getAddress());
            store.setPhoneNumber(updatedStore.getPhoneNumber());
            store.setDescription(updatedStore.getDescription());
            return storeRepository.save(store);
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

    // 가게 등록 요청 저장
    public StoreRequest registerStoreRequest(StoreRequest storeRequest) {
        return storeRequestRepository.save(storeRequest);
    }

    // 가게 등록 요청 승인
    public Store approveStore(Long requestId) {
        Optional<StoreRequest> storeRequestOpt = storeRequestRepository.findById(requestId);
        if (storeRequestOpt.isPresent()) {
            StoreRequest storeRequest = storeRequestOpt.get();
            Store store = new Store();
            store.setName(storeRequest.getName());
            store.setFoodType(storeRequest.getFoodType());
            store.setAddress(storeRequest.getAddress());
            store.setImageUrl(storeRequest.getImageUrl());
            store.setPhoneNumber(storeRequest.getPhoneNumber());
            store.setDescription(storeRequest.getDescription());
            store.setCompanyId(storeRequest.getCompanyId());
            store.setRegistrationDate(LocalDateTime.now());
            storeRepository.save(store);
            storeRequestRepository.delete(storeRequest);
            return store;
        } else {
            return null;
        }
    }

    // 모든 가게 등록 요청 조회
    public List<StoreRequest> getAllStoreRequests() {
        return storeRequestRepository.findAll();
    }
}

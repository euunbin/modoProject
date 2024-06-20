package com.example.modoproject.BusinessOwnerRegister.Service;

import com.example.modoproject.BusinessOwnerRegister.Repository.StoreRepository;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import com.example.modoproject.login.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private HttpSession httpSession;

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

    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    public List<Store> findFavoriteStores() {
        return storeRepository.findByFavoriteTrue();
    }

    public void toggleFavorite(String companyId) {
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            Store store = storeRepository.findByCompanyId(companyId);
            if (store != null) {
                boolean currentFavorite = store.isFavorite();
                store.setFavorite(!currentFavorite);
                storeRepository.save(store); // 즐겨찾기 상태 저장
            }
        }
    }
}
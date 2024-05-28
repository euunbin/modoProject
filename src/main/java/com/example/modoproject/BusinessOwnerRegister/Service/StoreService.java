package com.example.modoproject.BusinessOwnerRegister.Service;

import com.example.modoproject.BusinessOwnerRegister.Repository.StoreRepository;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    public Store registerStore(Store store) {
        store.setRegistrationDate(LocalDateTime.now());
        return storeRepository.save(store);
    }
}
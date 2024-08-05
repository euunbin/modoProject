package com.example.modoproject.BusinessOwnerRegister.Service;

import com.example.modoproject.BusinessOwnerRegister.Repository.StoreRepository;
import com.example.modoproject.BusinessOwnerRegister.Repository.StoreRequestRepository;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import com.example.modoproject.BusinessOwnerRegister.entity.StoreRequest;
import com.example.modoproject.Favorites.repository.FavoritesRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

    private HttpSession getSession() {
        return request.getSession();
    }

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
            store.setCompanyId(updatedStore.getCompanyId());
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

    public StoreRequest registerStoreRequest(StoreRequest storeRequest) {
        return storeRequestRepository.save(storeRequest);
    }

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
            store.setExternalId(storeRequest.getExternalId());
            store.setRegistrationDate(LocalDateTime.now());
            storeRepository.save(store);
            storeRequestRepository.delete(storeRequest);
            return store;
        } else {
            return null;
        }
    }


    public List<StoreRequest> getAllStoreRequests() {
        return storeRequestRepository.findAll();
    }

    public boolean deleteStore(String companyId) {
        Store store = storeRepository.findByCompanyId(companyId);
        if (store != null) {
            storeRepository.delete(store);
            return true;
        }
        return false;
    }

    public Store getStoreByExternalId(String externalId) {
        return storeRepository.findByExternalId(externalId);
    }

    public String getCompanyIdByExternalId(String externalId) {
        Store store = storeRepository.findByExternalId(externalId);
        return (store != null) ? store.getCompanyId() : "Not Found";
    }

    public void updateCompanyIdInSession(String newCompanyId) {
        HttpSession session = getSession();
        String externalId = (String) session.getAttribute("externalId");

        if (externalId != null) {
            Store store = storeRepository.findByExternalId(externalId);
            if (store != null) {
                String currentCompanyId = store.getCompanyId();
                logger.info("Current companyId in store: " + currentCompanyId);

                if (newCompanyId == null || newCompanyId.isEmpty()) {
                    if (currentCompanyId == null || !currentCompanyId.equals("업체 미등록")) {
                        store.setCompanyId("업체 미등록");
                        session.removeAttribute("companyId");
                        logger.info("Set companyId to '업체 미등록' and removed from session.");
                    }
                } else {
                    if (currentCompanyId == null || currentCompanyId.equals("업체 미등록") || !currentCompanyId.equals(newCompanyId)) {
                        store.setCompanyId(newCompanyId);
                        session.setAttribute("companyId", newCompanyId);
                        logger.info("Updated companyId in store to: " + newCompanyId);
                    }
                }
                storeRepository.save(store);
            } else {
                logger.info("Store not found for externalId: " + externalId);
            }
        } else {
            logger.info("No externalId found in session.");
        }
    }

}
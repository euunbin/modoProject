package com.example.modoproject.BusinessOwnerRegister.Repository;

import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByCompanyId(String companyId);

    List<Store> findByCompanyIdIn(List<String> companyIds);

    Store findByExternalId(String externalId);
}

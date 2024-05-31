package com.example.modoproject.BusinessOwnerRegister.Repository;

import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByCompanyId(String companyId);
}

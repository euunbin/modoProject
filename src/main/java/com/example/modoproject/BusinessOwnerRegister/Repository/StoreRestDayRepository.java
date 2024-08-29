package com.example.modoproject.BusinessOwnerRegister.Repository;

import com.example.modoproject.BusinessOwnerRegister.entity.StoreRestDay;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface StoreRestDayRepository extends JpaRepository<StoreRestDay, Long> {
    Optional<StoreRestDay> findByCompanyId(String companyId);
}
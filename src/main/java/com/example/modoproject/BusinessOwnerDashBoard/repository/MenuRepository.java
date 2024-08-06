package com.example.modoproject.BusinessOwnerDashBoard.repository;

import com.example.modoproject.BusinessOwnerDashBoard.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByCompanyId(String companyId);

    void deleteByCompanyId(String companyId);

}

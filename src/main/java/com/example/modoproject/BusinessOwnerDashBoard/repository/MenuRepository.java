package com.example.modoproject.BusinessOwnerDashBoard.repository;

import com.example.modoproject.BusinessOwnerDashBoard.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
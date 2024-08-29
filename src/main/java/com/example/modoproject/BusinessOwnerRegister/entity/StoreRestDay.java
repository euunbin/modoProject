package com.example.modoproject.BusinessOwnerRegister.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class StoreRestDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyId;

    @Column(columnDefinition = "TEXT")  // 일일 휴무일을 저장
    private String dailyRestDays;

    @Column(columnDefinition = "TEXT")  // 기간 휴무일을 저장
    private String periodRestDays;

    @Column(columnDefinition = "TEXT")  // 매주 반복되는 휴무일을 저장
    private String weeklyRestDays;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDailyRestDays() {
        return dailyRestDays;
    }

    public void setDailyRestDays(String dailyRestDays) {
        this.dailyRestDays = dailyRestDays;
    }

    public String getPeriodRestDays() {
        return periodRestDays;
    }

    public void setPeriodRestDays(String periodRestDays) {
        this.periodRestDays = periodRestDays;
    }

    public String getWeeklyRestDays() {
        return weeklyRestDays;
    }

    public void setWeeklyRestDays(String weeklyRestDays) {
        this.weeklyRestDays = weeklyRestDays;
    }
}


package com.example.modoproject.BusinessOwnerRegister.entity;


import java.util.List;

public class StoreRestDayRequest {

    private List<String> dailyRestDays;
    private List<String> periodRestDays;
    private List<String> weeklyRestDays;

    // Getters and Setters
    public List<String> getDailyRestDays() {
        return dailyRestDays;
    }

    public void setDailyRestDays(List<String> dailyRestDays) {
        this.dailyRestDays = dailyRestDays;
    }

    public List<String> getPeriodRestDays() {
        return periodRestDays;
    }

    public void setPeriodRestDays(List<String> periodRestDays) {
        this.periodRestDays = periodRestDays;
    }

    public List<String> getWeeklyRestDays() {
        return weeklyRestDays;
    }

    public void setWeeklyRestDays(List<String> weeklyRestDays) {
        this.weeklyRestDays = weeklyRestDays;
    }
}
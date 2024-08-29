package com.example.modoproject.BusinessOwnerRegister.Service;


import com.example.modoproject.BusinessOwnerRegister.Repository.StoreRestDayRepository;
import com.example.modoproject.BusinessOwnerRegister.entity.StoreRestDayRequest;
import com.example.modoproject.BusinessOwnerRegister.entity.StoreRestDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreRestDayService {

    @Autowired
    private StoreRestDayRepository storeRestDayRepository;

    public StoreRestDay saveOrUpdateRestDays(String companyId, String dailyRestDays, String periodRestDays, String weeklyRestDays) {
        Optional<StoreRestDay> existingRestDayOpt = storeRestDayRepository.findByCompanyId(companyId);

        StoreRestDay restDay;
        if (existingRestDayOpt.isPresent()) {
            // 기존 데이터가 존재하는 경우
            restDay = existingRestDayOpt.get();
            restDay.setDailyRestDays(dailyRestDays);
            restDay.setPeriodRestDays(periodRestDays);
            restDay.setWeeklyRestDays(weeklyRestDays);
        } else {
            // 새로운 데이터를 생성하는 경우
            restDay = new StoreRestDay();
            restDay.setCompanyId(companyId);
            restDay.setDailyRestDays(dailyRestDays);
            restDay.setPeriodRestDays(periodRestDays);
            restDay.setWeeklyRestDays(weeklyRestDays);
        }

        return storeRestDayRepository.save(restDay);
    }

    public StoreRestDay getRestDaysByCompanyId(String companyId) {
        return storeRestDayRepository.findByCompanyId(companyId).orElse(null);
    }
}

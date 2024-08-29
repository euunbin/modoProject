package com.example.modoproject.BusinessOwnerRegister.controller;


import com.example.modoproject.BusinessOwnerRegister.Service.StoreRestDayService;
import com.example.modoproject.BusinessOwnerRegister.entity.StoreRestDay;
import com.example.modoproject.BusinessOwnerRegister.entity.StoreRestDayRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rest-days")
public class StoreRestDayController {

    @Autowired
    private StoreRestDayService storeRestDayService;

    @Autowired
    private HttpSession session;

    @PostMapping
    public ResponseEntity<StoreRestDay> saveOrUpdateRestDays(@RequestBody StoreRestDayRequest request) {
        String companyId = (String) session.getAttribute("companyId");
        if (companyId == null) {
            return ResponseEntity.badRequest().build();
        }

        String dailyRestDays = request.getDailyRestDays() != null ?
                String.join(", ", request.getDailyRestDays().stream().map(date -> "D." + date).toList()) : null;

        String periodRestDays = request.getPeriodRestDays() != null ?
                String.join(", ", request.getPeriodRestDays().stream().map(period -> "P." + period).toList()) : null;

        String weeklyRestDays = request.getWeeklyRestDays() != null ?
                String.join(", ", request.getWeeklyRestDays()) : null;

        StoreRestDay savedRestDay = storeRestDayService.saveOrUpdateRestDays(companyId, dailyRestDays, periodRestDays, weeklyRestDays);
        return ResponseEntity.ok(savedRestDay);
    }

    @GetMapping("/current")
    public ResponseEntity<StoreRestDay> getCurrentRestDays() {
        String companyId = (String) session.getAttribute("companyId");
        if (companyId == null) {
            return ResponseEntity.badRequest().build();
        }

        StoreRestDay restDay = storeRestDayService.getRestDaysByCompanyId(companyId);
        if (restDay == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(restDay);
    }

    @GetMapping("/by-company")
    public ResponseEntity<StoreRestDay> getRestDaysByCompanyId(@RequestParam String companyId) {
        StoreRestDay restDay = storeRestDayService.getRestDaysByCompanyId(companyId);
        if (restDay == null) {
            // 빈 StoreRestDay 객체를 반환하여 클라이언트 측에서 404를 처리하지 않도록 함
            restDay = new StoreRestDay();
            restDay.setCompanyId(companyId);
            restDay.setDailyRestDays("");
            restDay.setPeriodRestDays("");
            restDay.setWeeklyRestDays("");
            return ResponseEntity.ok(restDay);
        }

        return ResponseEntity.ok(restDay);
    }


}
package com.example.modoproject.BusinessOwnerDashBoard.service;

import com.example.modoproject.BusinessOwnerDashBoard.repository.MenuRepository;
import com.example.modoproject.BusinessOwnerDashBoard.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Menu saveMenu(String companyId, String name, int price, MultipartFile image) throws IOException {
        Menu menu = new Menu();
        menu.setCompanyId(companyId);
        menu.setName(name);
        menu.setPrice(price);
        menu.setCreatedAt(new Date());
        menu.setMerchanUid(generateMerchanUid()); // 랜덤한 merchanUid 생성

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            menu.setImageUrl(imageUrl);
        }

        return menuRepository.save(menu);
    }

    private String generateMerchanUid() {
        // 숫자와 영문 대소문자를 조합하여 20자의 랜덤한 문자열 생성
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(20);
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }


    // 이미지를 저장하는 메서드
    private String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return null; // 이미지가 없는 경우 null을 반환합니다.
        }

        // 이미지를 저장할 디렉토리 경로를 생성합니다.
        String directoryPath = "src/main/resources/static/menuimage";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 존재하지 않으면 생성합니다.
        }

        // 파일 이름을 생성합니다 (중복을 피하기 위해 현재 시간을 이름에 포함).
        String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(directoryPath, filename);

        // 파일을 저장합니다.
        image.transferTo(filePath);

        // 저장된 이미지의 URL을 반환합니다.
        return "/menuimage/" + filename; // 정적 리소스 URL을 생성합니다.
    }

    public List<Menu> getMenuByStoreId(String companyId) {
        return menuRepository.findByCompanyId(companyId);
    }
}

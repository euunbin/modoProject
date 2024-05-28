package com.example.modoproject.BusinessOwnerDashBoard.service;

import com.example.modoproject.BusinessOwnerDashBoard.entity.Menu;
import com.example.modoproject.BusinessOwnerDashBoard.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service // 이 클래스가 서비스 계층의 컴포넌트임을 나타냅니다.
public class MenuService {
    @Autowired // 스프링 프레임워크가 MenuRepository를 자동으로 주입합니다.
    private MenuRepository menuRepository;

    // 모든 메뉴를 조회하는 메서드
    public List<Menu> getAllMenus() {
        return menuRepository.findAll(); // 데이터베이스에서 모든 메뉴 항목을 가져옵니다.
    }

    // 새로운 메뉴를 저장하는 메서드
    public Menu saveMenu(Long companyId, String name, double price, MultipartFile image) throws IOException {
        Menu menu = new Menu(); // 새로운 Menu 객체를 생성합니다.
        menu.setCompanyId(companyId); // 회사 ID를 설정합니다.
        menu.setName(name); // 메뉴 이름을 설정합니다.
        menu.setPrice(price); // 메뉴 가격을 설정합니다.
        menu.setCreatedAt(new Date()); // 메뉴 생성 시간을 현재 시간으로 설정합니다.

        // 이미지가 있는 경우 이미지를 저장하고 URL을 설정합니다.
        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image); // 이미지를 저장하고 URL을 반환받습니다.
            menu.setImageUrl(imageUrl); // 메뉴 객체에 이미지 URL을 설정합니다.
        }

        return menuRepository.save(menu); // 메뉴 객체를 데이터베이스에 저장하고 반환합니다.
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
}

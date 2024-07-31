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
import java.util.Optional;
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
        menu.setMerchanUid(generateMerchanUid());

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            menu.setImageUrl(imageUrl);
        }

        return menuRepository.save(menu);
    }

    public Menu updateMenu(Long id, String companyId, String name, int price, MultipartFile image) throws IOException {
        Optional<Menu> optionalMenu = menuRepository.findById(id);
        if (!optionalMenu.isPresent()) {
            throw new IllegalArgumentException("Invalid menu ID");
        }
        Menu menu = optionalMenu.get();
        menu.setCompanyId(companyId);
        menu.setName(name);
        menu.setPrice(price);

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            menu.setImageUrl(imageUrl);
        }

        return menuRepository.save(menu);
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    private String generateMerchanUid() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(20);
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return null;
        }

        String directoryPath = "src/main/resources/static/menuimage";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(directoryPath, filename);
        image.transferTo(filePath);

        return "/menuimage/" + filename;
    }
}

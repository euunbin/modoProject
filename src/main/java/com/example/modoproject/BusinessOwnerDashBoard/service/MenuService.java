package com.example.modoproject.BusinessOwnerDashBoard.service;

import com.example.modoproject.BusinessOwnerDashBoard.entity.Menu;
import com.example.modoproject.BusinessOwnerDashBoard.repository.MenuRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    public void updateMenuCompanyId(String oldCompanyId, String newCompanyId) {
        List<Menu> menus = menuRepository.findByCompanyId(oldCompanyId);

        if (menus != null && !menus.isEmpty()) {
            for (Menu menu : menus) {
                if (menu.getCompanyId().equals(oldCompanyId)) {
                    menu.setCompanyId(newCompanyId);
                    menuRepository.save(menu);
                }
            }
        }
    }

    public List<Menu> getMenusByCompanyId(String companyId) {
        // Implement the logic to fetch menus by companyId
        return menuRepository.findByCompanyId(companyId);
    }
    @Transactional
    public void deleteMenusByCompanyId(String companyId) {
        menuRepository.deleteByCompanyId(companyId);
    }

    public List<Menu> getRandomMenus(int count) {
        List<Menu> menuList = menuRepository.findAll();

        if (menuList.isEmpty()) {
            return Collections.emptyList();
        }

        int bound = menuList.size();
        if (count <= 0 || count > bound) {
            throw new IllegalArgumentException("Count must be between 1 and " + bound);
        }

        Random random = new Random();
        Set<Menu> randomMenus = new HashSet<>();

        while (randomMenus.size() < count) {
            int index = random.nextInt(bound);
            randomMenus.add(menuList.get(index));
        }

        return new ArrayList<>(randomMenus);
    }


}
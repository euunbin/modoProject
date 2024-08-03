package com.example.modoproject.BusinessOwnerRegister.controller;

import com.example.modoproject.BusinessOwnerDashBoard.entity.Menu;
import com.example.modoproject.BusinessOwnerRegister.Service.StoreService;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import com.example.modoproject.BusinessOwnerDashBoard.repository.MenuRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private MenuRepository menuRepository;

    // 뷰 관련 메서드
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("store", new Store());
        return "register";
    }

    @GetMapping("/stores")
    public String showStoreList(Model model, HttpSession session) {
        List<Store> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);
        model.addAttribute("externalId", session.getAttribute("externalId"));
        return "storeList";
    }

    @PostMapping("/stores")
    public String registerStore(@ModelAttribute Store store, HttpSession session) {
        String externalId = (String) session.getAttribute("externalId");
        store.setExternalId(externalId);

        storeService.registerStore(store);
        return "redirect:/stores";
    }

    @GetMapping("/stores/edit/{companyId}")
    public String showEditForm(@PathVariable String companyId, Model model) {
        Store store = storeService.findByCompanyId(companyId);
        if (store != null) {
            model.addAttribute("store", store);
            return "storeupdate";
        } else {
            return "redirect:/stores"; // 가게를 찾을 수 없는 경우
        }
    }

    @PostMapping("/stores/edit/{companyId}")
    public String updateStore(@PathVariable String companyId, @ModelAttribute Store store) {
        storeService.updateStore(companyId, store);
        return "redirect:/stores";
    }

    @RestController
    @RequestMapping("/api/stores")
    public class StoreApiController {

        @Autowired
        private StoreService storeService;

        @Autowired
        private HttpSession session;

        private final Path uploadDir = Paths.get("src/main/resources/static/storeImg");

        @PostMapping
        public ResponseEntity<Store> registerStore(@RequestBody Store store) {
            String externalId = (String) session.getAttribute("externalId");
            if (externalId == null) {
                return ResponseEntity.badRequest().build();
            }
            store.setExternalId(externalId);
            Store savedStore = storeService.registerStore(store);
            return ResponseEntity.ok(savedStore);
        }

        @PutMapping("/{companyId}")
        public ResponseEntity<Store> updateStore(@PathVariable String companyId, @RequestBody Store store) {
            try {
                Store updatedStore = storeService.updateStore(companyId, store);
                if (updatedStore == null) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(updatedStore);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }


        @GetMapping("/by-external-id")
        public ResponseEntity<Store> getStoreByExternalId() {
            String externalId = (String) session.getAttribute("externalId");
            if (externalId == null) {
                return ResponseEntity.notFound().build();
            }
            Store store = storeService.getStoreByExternalId(externalId);
            return ResponseEntity.ok(store);
        }

        @PostMapping("/upload-image")
        public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 없습니다.");
            }

            try {
                String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
                Path filePath = saveImage(file, fileName);

                String fileUrl = "/storeImg/" + fileName;
                return ResponseEntity.ok(fileUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 저장 실패");
            }
        }

        private Path saveImage(MultipartFile image, String fileName) throws IOException {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath;
        }

        @GetMapping("/storeImg/{fileName}")
        public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
            Path filePath = uploadDir.resolve(fileName);
            try {
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists() || resource.isReadable()) {
                    String contentType = Files.probeContentType(filePath);
                    if (contentType == null) {
                        contentType = "application/octet-stream";
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .body(resource);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return ResponseEntity.notFound().build();
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}

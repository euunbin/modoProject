package com.example.modoproject.BusinessOwnerRegister.controller;

import com.example.modoproject.BusinessOwnerDashBoard.entity.Menu;
import com.example.modoproject.BusinessOwnerDashBoard.repository.MenuRepository;
import com.example.modoproject.BusinessOwnerDashBoard.service.MenuService;
import com.example.modoproject.BusinessOwnerRegister.Repository.StoreRepository;
import com.example.modoproject.BusinessOwnerRegister.Service.StoreService;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import com.example.modoproject.BusinessOwnerRegister.entity.StoreRequest;
import com.example.modoproject.Review.entity.Review;
import com.example.modoproject.Review.service.ReviewService;
import com.example.modoproject.login.service.oauth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private StoreService storeService;

    @Autowired
    private HttpSession session;

    @Autowired
    private MenuService menuService; // MenuService 주입

    @Autowired
    private oauth2UserService oAuth2UserService;



    private final Path uploadDir = Paths.get("src/main/resources/static/storeImg");

    @PostMapping
    public ResponseEntity<StoreRequest> registerStoreRequest(@RequestBody StoreRequest storeRequest) {
        String externalId = (String) session.getAttribute("externalId");
        if (externalId == null) {
            return ResponseEntity.badRequest().build();
        }
        storeRequest.setExternalId(externalId);
        StoreRequest newStoreRequest = storeService.registerStoreRequest(storeRequest);
        return ResponseEntity.ok(newStoreRequest);
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

    @PutMapping("/approve/{requestId}")
    public ResponseEntity<Store> approveStore(@PathVariable Long requestId) {
        Store store = storeService.approveStore(requestId);
        if (store != null) {
            String externalId = store.getExternalId();
            if (externalId != null) {
                // User의 Role을 ROLE_OWNER로 변경
                oAuth2UserService.updateUserRoleToOwner(externalId);
            }
            String companyId = store.getCompanyId();
            session.setAttribute("companyId", companyId);
            logger.info("Current companyId in session: " + companyId); // 로그 출력
            return ResponseEntity.ok(store);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<Store> updateStore(@PathVariable String companyId, @RequestBody Store store) {
        try {
            Store updatedStore = storeService.updateStore(companyId, store);
            if (updatedStore != null) {
                String newCompanyId = updatedStore.getCompanyId();

                if (!companyId.equals(newCompanyId)) {
                    menuService.updateMenuCompanyId(companyId, newCompanyId);
                    session.setAttribute("companyId", newCompanyId);
                    logger.info("Updated companyId in session to: " + newCompanyId);
                }

                return ResponseEntity.ok(updatedStore);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteStore(@PathVariable String companyId) {
        boolean isDeleted = storeService.deleteStore(companyId);
        if (isDeleted) {
            menuService.deleteMenusByCompanyId(companyId);

            // 세션에서 companyId 삭제
            if (session.getAttribute("companyId") != null) {
                session.removeAttribute("companyId");
                logger.info("Deleted companyId from session: " + companyId);
            }
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/requests")
    public ResponseEntity<List<StoreRequest>> getAllStoreRequests() {
        List<StoreRequest> storeRequests = storeService.getAllStoreRequests();
        return ResponseEntity.ok(storeRequests);
    }

    @GetMapping
    public ResponseEntity<List<Store>> getAllStores() {
        List<Store> stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }
    @GetMapping("/{id}") // StoreList에 사용
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        Optional<Store> store = storeService.findById(id);
        return store.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{companyId}/menu") //StoreDetail에 사용
    public ResponseEntity<List<Menu>> getMenuByStoreCompanyId(@PathVariable String companyId) {
        List<Menu> menu = menuRepository.findByCompanyId(companyId);
        return ResponseEntity.ok(menu);
    }

    @GetMapping("/{companyId}/name")
    public ResponseEntity<String> getStoreNameByCompanyId(@PathVariable String companyId) {
        Store store = storeService.findByCompanyId(companyId);
        if (store != null) {
            return ResponseEntity.ok(store.getName());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my-store-reviews")
    public ResponseEntity<List<Review>> getMyStoreReviews() {
        String companyId = (String) session.getAttribute("companyId");

        if (companyId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 세션에 companyId가 없는 경우
        }

        List<Review> reviews = reviewService.findByCompanyId(companyId);

        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 리뷰가 없는 경우
        }

        return ResponseEntity.ok(reviews);
    }
}
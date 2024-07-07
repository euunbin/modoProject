package com.example.modoproject.BusinessOwnerDashBoard.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import com.example.modoproject.BusinessOwnerDashBoard.entity.Menu;
import com.example.modoproject.BusinessOwnerDashBoard.service.MenuService;

@Controller // 이 클래스가 컨트롤러 역할을 한다는 것을 나타냅니다.
@RequestMapping("/menus") // 이 컨트롤러의 모든 요청 매핑이 "/menus" 경로에 바인딩됩니다.
public class MenuController {

    @Autowired // 스프링 프레임워크가 MenuService를 자동으로 주입합니다.
    private MenuService menuService;

    @GetMapping("/add") // "/menus/add" 경로에 대한 GET 요청을 처리합니다.
    public String showAddMenuForm() {
        return "menuadd"; // "menuadd.html" 뷰를 반환합니다.
    }

    @GetMapping("/list") // "/menus/list" 경로에 대한 GET 요청을 처리합니다.
    public String showMenuList(Model model) {
        List<Menu> menuList = menuService.getAllMenus(); // 모든 메뉴를 가져옵니다.
        model.addAttribute("menuList", menuList); // 모델에 메뉴 목록을 추가합니다.
        return "menulist"; // "menulist.html" 뷰를 반환합니다.
    }

    @PostMapping("/add") // "/menus/add" 경로에 대한 POST 요청을 처리합니다.
    public String addMenu(@RequestParam("companyId") String companyId,
                          @RequestParam("names") String[] names,
                          @RequestParam("prices") String[] prices,
                          @RequestParam("images") MultipartFile[] images,
                          @RequestParam("index") int index) throws IOException {
        if (index < 0 || index >= images.length) {
            throw new IllegalArgumentException("Invalid image index"); // 유효하지 않은 인덱스에 대한 예외 처리
        }
        menuService.saveMenu(companyId, names[index], Integer.parseInt(prices[index]), images[index]);
        return "redirect:/menus/add"; // 메뉴 추가 후 "menuadd" 페이지로 리디렉션합니다.
    }

    @PostMapping("/addAll") // "/menus/addAll" 경로에 대한 POST 요청을 처리합니다.
    public String addAllMenus(@RequestParam("companyId") String companyId,
                              @RequestParam("names") String[] names,
                              @RequestParam("prices") String[] prices,
                              @RequestParam("images") MultipartFile[] images) throws IOException {
        for (int i = 0; i < names.length; i++) {
            MultipartFile image = (images.length > i) ? images[i] : null; // 이미지 배열의 범위를 벗어나지 않도록 합니다.
            menuService.saveMenu(companyId, names[i], Integer.parseInt(prices[i]), image); // 메뉴를 저장합니다.
        }
        return "redirect:/menus/add"; // 모든 메뉴 추가 후 "menuadd" 페이지로 리디렉션합니다.
    }
    @GetMapping("/{merchanUid}/{menuName}/{price}")
    public String showMenuDetails(@PathVariable String merchanUid,
                                  @PathVariable String menuName,
                                  @PathVariable int price,
                                  Model model) {
        model.addAttribute("merchanUid", merchanUid);
        model.addAttribute("menuName", menuName);
        model.addAttribute("price", price);
        return "menudetails";
    }
}

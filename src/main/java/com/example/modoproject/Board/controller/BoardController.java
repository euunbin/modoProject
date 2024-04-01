package com.example.modoproject.Board.controller;

import com.example.modoproject.Board.dto.BoardDto;

import com.example.modoproject.Board.service.BoardService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class BoardController {
    private BoardService boardService;
    private static final String UPLOAD_DIR = "uploads"; // 이미지를 업로드할 디렉토리

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String list(Model model) {
        List<BoardDto> boardDtoList = boardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "board/list.html";
    }

    @GetMapping("/post")
    public String post() {

        return "board/post.html";
    }



    @PostMapping("/post")
    public String write(BoardDto boardDto, @RequestParam("image") MultipartFile image) {
        try {
            // 이미지 데이터를 바이트 배열로 변환하여 BoardDto에 설정
            boardDto.setImageData(image.getBytes());
            // 이미지를 저장하고 저장된 경로를 imagePath에 설정
            String imagePath = saveImage(image);
            boardDto.setImagePath(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            // 이미지 데이터를 읽어오는데 실패한 경우 예외 처리
            // 실패 시에는 그냥 빈 이미지로 저장되거나, 에러 메시지를 보여주고 다시 입력받는 등의 처리가 필요할 수 있습니다.
        }

        boardService.savePost(boardDto);
        return "redirect:/";
    }


    private String saveImage(MultipartFile image) {
        // 업로드 디렉토리가 없다면 생성
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // 파일명 중복을 피하기 위해 UUID를 이용하여 고유한 파일명 생성
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString() + "_" + fileName;

        // 업로드할 경로 설정
        Path uploadPath = Paths.get(UPLOAD_DIR);

        try {
            // 파일 저장
            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(image.getInputStream(), filePath);

            // 저장된 파일의 경로를 반환
            return UPLOAD_DIR + File.separator + newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 저장에 실패한 경우 예외 처리
            return null;
        }
    }

    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/detail.html";
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/edit.html";
    }

    @PutMapping("/post/edit/{id}")
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        return "redirect:/";
    }
}
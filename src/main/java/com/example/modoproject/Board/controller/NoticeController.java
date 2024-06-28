package com.example.modoproject.Board.controller;

import com.example.modoproject.Board.dto.BoardDto;
import com.example.modoproject.Board.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    private final BoardService boardService;
    private static final String UPLOAD_DIR = "src/main/resources/static/boardImg/notice";

    public NoticeController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public ResponseEntity<List<BoardDto>> list() {
        List<BoardDto> noticeList = boardService.getBoardList().stream()
                .filter(post -> "공지사항".equals(post.getType()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(noticeList);
    }

    @PostMapping
    public ResponseEntity<BoardDto> write(@RequestPart("board") BoardDto boardDto, @RequestPart("image") MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                boardDto.setImagePath(saveImage(image));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        boardDto.setType("공지사항");
        if (boardDto.getCategory() == null) {
            boardDto.setCategory("default-category"); // 기본 카테고리 설정
        }
        Long id = boardService.savePost(boardDto);
        return ResponseEntity.ok(boardService.getPost(id));
    }

    private String saveImage(MultipartFile image) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.copy(image.getInputStream(), filePath);
        return "/boardImg/notice/" + fileName;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> detail(@PathVariable("id") Long id) {
        BoardDto boardDto = boardService.getPost(id);
        return ResponseEntity.ok(boardDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDto> update(@PathVariable("id") Long id, @RequestPart("board") BoardDto boardDto, @RequestPart(value = "image", required = false) MultipartFile image) {
        BoardDto existingPost = boardService.getPost(id);

        if (existingPost != null) {
            boardDto.setId(id); // ID 설정
            boardDto.setType(existingPost.getType()); // 기존 type 값을 유지

            try {
                if (image != null && !image.isEmpty()) {
                    boardDto.setImagePath(saveImage(image));
                } else {
                    boardDto.setImagePath(existingPost.getImagePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            boardService.savePost(boardDto);
            return ResponseEntity.ok(boardService.getPost(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<BoardDto>> getPostsByCategory(@PathVariable("category") String category) {
        List<BoardDto> boardDtoList = boardService.getBoardListByCategory(category);
        return ResponseEntity.ok(boardDtoList);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<BoardDto>> search(@PathVariable("keyword") String keyword) {
        List<BoardDto> searchResults = boardService.searchPosts(keyword);
        return ResponseEntity.ok(searchResults);
    }
}

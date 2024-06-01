package com.example.modoproject.Board.dto;

import com.example.modoproject.Board.entity.Board;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String author;
    private String title;
    private String content;
    private String imagePath; // 이미지 경로 필드 추가
    private String category;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private MultipartFile image;
    private String type;
    private byte[] imageData; // 이미지 데이터 필드 추가
    public static BoardDto from(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .author(board.getAuthor())
                .title(board.getTitle())
                .content(board.getContent())
                .imagePath(board.getImagePath())
                .category(board.getCategory())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .type(board.getType())
                .build();
    }

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .author(author)
                .title(title)
                .content(content)
                .imagePath(imagePath) // Board 엔티티에 이미지 경로 설정
                .category(category)
                .type(type)
                .build();
    }

    @Builder
    public BoardDto(Long id, String author, String title, String content, String imagePath, String category, LocalDateTime createdDate, LocalDateTime modifiedDate, String type) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.category = category;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.type = type;

    }

    // 이미지 데이터를 저장하는 setter
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }
}

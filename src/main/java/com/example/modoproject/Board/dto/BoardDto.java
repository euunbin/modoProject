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
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private MultipartFile image;
    private byte[] imageData; // 이미지 데이터 필드 추가

    public Board toEntity() {
        Board build = Board.builder()
                .id(id)
                .author(author)
                .title(title)
                .content(content)
                .imagePath(imagePath) // Board 엔티티에 이미지 경로 설정
                .build();
        return build;
    }

    @Builder
    public BoardDto(Long id, String author, String title, String content, String imagePath, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    // 이미지 데이터를 저장하는 setter
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    // 이미지 경로를 설정하는 setter
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // 이미지 경로를 반환하는 getter
    public String getImagePath() {
        return imagePath;
    }
}

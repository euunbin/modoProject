package com.example.modoproject.Board.service;

import com.example.modoproject.Board.dto.BoardDto;
import com.example.modoproject.Board.entity.Board;
import com.example.modoproject.Board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        Board board;
        if (boardDto.getId() != null) {
            board = boardRepository.findById(boardDto.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
            boardDto.setType(board.getType()); // 기존 type 값을 유지
        }
        board = boardDto.toEntity();
        return boardRepository.save(board).getId();
    }

    @Transactional
    public List<BoardDto> getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (Board board : boardList) {
            BoardDto boardDto = BoardDto.builder()
                    .id(board.getId())
                    .author(board.getAuthor())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .imagePath(board.getImagePath())
                    .category(board.getCategory())
                    .createdDate(board.getCreatedDate())
                    .type(board.getType()) // type 추가
                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    @Transactional
    public BoardDto getPost(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .author(board.getAuthor())
                .title(board.getTitle())
                .content(board.getContent())
                .imagePath(board.getImagePath())
                .category(board.getCategory())
                .createdDate(board.getCreatedDate())
                .type(board.getType()) // type 추가
                .build();
        return boardDto;
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public List<BoardDto> getBoardListByCategory(String category) {
        List<Board> boardList = boardRepository.findByCategory(category);
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (Board board : boardList) {
            BoardDto boardDto = BoardDto.builder()
                    .id(board.getId())
                    .author(board.getAuthor())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .imagePath(board.getImagePath())
                    .category(board.getCategory())
                    .createdDate(board.getCreatedDate())
                    .type(board.getType()) // type 추가
                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    public List<BoardDto> searchPosts(String keyword) {
        List<Board> searchResults = boardRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        List<BoardDto> searchResultsDto = new ArrayList<>();
        for (Board board : searchResults) {
            searchResultsDto.add(BoardDto.from(board));
        }
        return searchResultsDto;
    }
}

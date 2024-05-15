package com.example.modoproject.Board.repository;


import com.example.modoproject.Board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByCategory(String category);

    List<Board> findByTitleContainingOrContentContaining(String title, String content);
}

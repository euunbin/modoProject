package com.example.modoproject.Board.repository;


import com.example.modoproject.Board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
package com.study.board2.repository;

import com.study.board2.entity.Board2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board2, Integer> {

    Page<Board2> findByTitleContaining(String searchKeyword, Pageable pageable); // 메서드 추가
}

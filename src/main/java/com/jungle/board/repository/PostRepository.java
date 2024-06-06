package com.jungle.board.repository;

import com.jungle.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findAllByOrderByModifiedAtDesc();
}

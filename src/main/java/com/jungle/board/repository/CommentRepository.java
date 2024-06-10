package com.jungle.board.repository;

import com.jungle.board.entity.Comment;
import com.jungle.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findAllByOrderByModifiedAtDesc();
    @Transactional
    void deleteByPostId(Long postId);

}

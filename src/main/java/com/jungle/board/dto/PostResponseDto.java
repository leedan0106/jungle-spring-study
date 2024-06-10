package com.jungle.board.dto;

import com.jungle.board.entity.Post;
import com.jungle.board.repository.CommentRepository;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;

    private List<CommentResponseDto> commentList;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();

        this.commentList = post.getComments().stream()
                .map(comment -> new CommentResponseDto(comment))
                .collect(Collectors.toList());
    }
}

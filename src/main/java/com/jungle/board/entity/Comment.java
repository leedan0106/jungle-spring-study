package com.jungle.board.entity;

import com.jungle.board.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@Entity(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String contents;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public Comment(CommentRequestDto requestDto, User user, Post post) {
        this.contents = requestDto.getContents();
        this.user = user;
        this.post = post;
    }

}

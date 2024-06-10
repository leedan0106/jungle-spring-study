package com.jungle.board.controller;

import com.jungle.board.dto.CommentRequestDto;
import com.jungle.board.dto.CommentResponseDto;
import com.jungle.board.dto.ResponseDto;
import com.jungle.board.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comments/{postId}")
    public CommentResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(postId, requestDto, request);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseDto deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        return commentService.deleteComment(commentId, request);
    }
}

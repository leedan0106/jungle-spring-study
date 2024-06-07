package com.jungle.board.controller;

import com.jungle.board.dto.PostDeleteRequestDto;
import com.jungle.board.dto.PostRequestDto;
import com.jungle.board.dto.PostResponseDto;
import com.jungle.board.dto.ResponseDto;
import com.jungle.board.entity.Post;
import com.jungle.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    @GetMapping("/posts")
    public List<PostResponseDto> getPostList() {
        return postService.getPostList();
    }

    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        return postService.updatePost(id, requestDto);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseDto deletePost(@PathVariable Long id, @RequestBody PostDeleteRequestDto requestDto) {
        return postService.deletePost(id, requestDto);
    }
}

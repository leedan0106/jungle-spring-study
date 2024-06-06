package com.jungle.board.service;

import com.jungle.board.dto.PostRequestDto;
import com.jungle.board.dto.PostResponseDto;
import com.jungle.board.entity.Post;
import com.jungle.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);
        postRepository.save(post);
        return post;
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostList() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream()
                .map(post -> new PostResponseDto(post))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        // 패스워드 일치 여부 확인..
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    @Transactional
    public Long deleteMemo(Long id) {
        postRepository.deleteById(id);
        // response 따로 만들어서 return 필요.
        // 에러 처리도 필요.
        return id;
    }
}

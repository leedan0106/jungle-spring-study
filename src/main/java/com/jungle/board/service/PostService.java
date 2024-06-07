package com.jungle.board.service;

import com.jungle.board.dto.PostDeleteRequestDto;
import com.jungle.board.dto.PostRequestDto;
import com.jungle.board.dto.PostResponseDto;
import com.jungle.board.dto.ResponseDto;
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
    public PostResponseDto createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);
        postRepository.save(post);
        return new PostResponseDto(post);
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
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        // 패스워드 일치 해야 수정 가능
        String password = requestDto.getPassword();
        if(!post.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        post.update(requestDto);
        return new PostResponseDto(post);
    }

    @Transactional
    public ResponseDto deletePost(Long id, PostDeleteRequestDto requestDto) { // password를 받아와야 한다. dto가 또 필요함.
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        // password 일치해야 삭제 가능
        String password = requestDto.getPassword();
        if(!post.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        postRepository.deleteById(id);
        // 에러 처리도 필요.
        return new ResponseDto("Success", 200);
    }
}

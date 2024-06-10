package com.jungle.board.service;

import com.jungle.board.dto.PostRequestDto;
import com.jungle.board.dto.PostResponseDto;
import com.jungle.board.dto.ResponseDto;
import com.jungle.board.entity.Post;
import com.jungle.board.entity.User;
import com.jungle.board.entity.UserRoleEnum;
import com.jungle.board.repository.CommentRepository;
import com.jungle.board.repository.PostRepository;
import com.jungle.board.jwt.JwtUtil;
import com.jungle.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        // Token 확인
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            }else {
                throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
            }

            // 사용자 존재하는지 확인
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Post post = new Post(requestDto, user);
            postRepository.save(post);
            return new PostResponseDto(post);
        }else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
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
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
            }

            // 사용자 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Post post = postRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
            );

            if(user.getRole() == UserRoleEnum.USER) { // admin은 모든 게시물 수정 가능
                // 사용자가 작성한 게시물인지 확인
                if (!user.getUsername().equals(post.getUser().getUsername())) {
                    throw new IllegalArgumentException("사용자가 작성한 게시물이 아닙니다.");
                }
            }

            post.update(requestDto);
            return new PostResponseDto(post);
        } else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
    }

    @Transactional
    public ResponseDto deletePost(Long id, HttpServletRequest request) { // password를 받아와야 한다. dto가 또 필요함.
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
            }

            // 사용자 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Post post = postRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
            );

            if (user.getRole() != UserRoleEnum.USER) {
                // 사용자가 작성한 게시물인지 확인
                if (!user.getUsername().equals(post.getUser().getUsername())) {
                    throw new IllegalArgumentException("사용자가 작성한 게시물이 아닙니다.");
                }
            }

            commentRepository.deleteByPostId(id);
            postRepository.deleteById(id);
            return new ResponseDto("Success", HttpStatus.OK);

        }else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }

    }
}

package com.jungle.board.service;

import com.jungle.board.dto.CommentRequestDto;
import com.jungle.board.dto.CommentResponseDto;
import com.jungle.board.dto.ResponseDto;
import com.jungle.board.entity.Comment;
import com.jungle.board.entity.Post;
import com.jungle.board.entity.User;
import com.jungle.board.entity.UserRoleEnum;
import com.jungle.board.jwt.JwtUtil;
import com.jungle.board.repository.CommentRepository;
import com.jungle.board.repository.PostRepository;
import com.jungle.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
          if (jwtUtil.validateToken(token)) {
              claims = jwtUtil.getUserInfoFromToken(token);
          } else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
          }

          User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                  () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
          );

          Post post = postRepository.findById(postId).orElseThrow(
                  () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
          );

          Comment comment = new Comment(requestDto, user, post);
          commentRepository.save(comment);
          return new CommentResponseDto(comment);
        } else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
    }

    @Transactional
    public ResponseDto deleteComment(Long commentId, HttpServletRequest request) {
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

            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
            );

            if(user.getRole() != UserRoleEnum.USER) {
                if (!user.getUsername().equals(comment.getUser().getUsername())) {
                    throw new IllegalArgumentException("사용자가 작성한 댓글이 아닙니다.");
                }
            }

            commentRepository.deleteById(commentId);
            return new ResponseDto("Success", HttpStatus.OK);

        } else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
    }
}

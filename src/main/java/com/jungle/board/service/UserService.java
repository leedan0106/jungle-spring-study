package com.jungle.board.service;

import com.jungle.board.dto.ResponseDto;
import com.jungle.board.dto.UserRequestDto;
import com.jungle.board.jwt.JwtUtil;
import com.jungle.board.repository.UserRepository;
import com.jungle.board.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseDto signin(UserRequestDto requestDto) {
        // username 중복 확인
        Optional<User> found = userRepository.findByUsername(requestDto.getUsername());
        if (found.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 Username입니다.");
        }

        User user = new User(requestDto);
        userRepository.save(user);
        return new ResponseDto("Success", 200);
    }

    @Transactional
    public ResponseDto login(UserRequestDto requestDto, HttpServletResponse response) {
        // username 존재 유무 확인
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 Username입니다.")
        );

        // password 비교
        if(!user.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 로그인 성공시 성공한 유저 정보와 JWT 토큰 발급.
        // 발급한 토큰을 Header에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
        return new ResponseDto("Success", 200);
    }


}

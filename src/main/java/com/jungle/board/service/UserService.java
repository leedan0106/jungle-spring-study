package com.jungle.board.service;

import com.jungle.board.config.AuthConfig;
import com.jungle.board.dto.ResponseDto;
import com.jungle.board.dto.UserRequestDto;
import com.jungle.board.entity.UserRoleEnum;
import com.jungle.board.jwt.JwtUtil;
import com.jungle.board.repository.UserRepository;
import com.jungle.board.entity.User;
import com.jungle.board.config.AuthConfig.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseDto signup(UserRequestDto requestDto) {
        // username 중복 확인
        Optional<User> found = userRepository.findByUsername(requestDto.getUsername());
        if (found.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 Username입니다.");
        }

        // 사용자 권한 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if(!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀렸습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        String password = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto.getUsername(), password, role);

        userRepository.save(user);
        return new ResponseDto("Success", HttpStatus.OK);
    }

    @Transactional
    public ResponseDto login(UserRequestDto requestDto, HttpServletResponse response) {
        // username 존재 유무 확인
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("회원을 찾을 수 없습니다.")
        );

        // password 비교
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

//        if(!user.getPassword().equals(requestDto.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }

        // 로그인 성공시 성공한 유저 정보와 JWT 토큰 발급.
        // 발급한 토큰을 Header에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return new ResponseDto("Success", HttpStatus.OK);
    }


}

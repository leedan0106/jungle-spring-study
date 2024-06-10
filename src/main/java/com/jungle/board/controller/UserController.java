package com.jungle.board.controller;

import com.jungle.board.dto.ResponseDto;
import com.jungle.board.dto.UserRequestDto;
import com.jungle.board.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody @Valid UserRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public ResponseDto login(@RequestBody UserRequestDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }

}

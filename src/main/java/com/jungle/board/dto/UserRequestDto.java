package com.jungle.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;

@Getter
public class UserRequestDto {

    @NotBlank(message = "아이디가 없습니다.")
    @Size(min = 4, max = 10, message = "아이디는 4 ~ 10 글자여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "아이디는 알파벳 소문자와 숫자만 포함 가능합니다.")
    private String username;

    @NotBlank(message = "비밀번호가 없습니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8 ~ 15 글자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 알파벳 대/소문자와 숫자만 포함 가능합니다.")
    private String password;

    private boolean admin = false;
    private String adminToken = "";
}
package com.jungle.board.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDto {
    private String message;
    private Integer statusCode;

    public ResponseDto(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode.value();
    }
}

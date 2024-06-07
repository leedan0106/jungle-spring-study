package com.jungle.board.dto;

import lombok.Getter;

@Getter
public class ResponseDto {
    private String message;
    private Integer statusCode;

    public ResponseDto(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}

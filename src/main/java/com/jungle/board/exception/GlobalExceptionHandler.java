package com.jungle.board.exception;

import com.jungle.board.dto.ResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseDto handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

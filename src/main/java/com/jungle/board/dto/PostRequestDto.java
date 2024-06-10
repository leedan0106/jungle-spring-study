package com.jungle.board.dto;

import com.jungle.board.entity.User;
import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    private String content;
}

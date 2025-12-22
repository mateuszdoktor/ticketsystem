package com.example.ticketsystem.dto.comment;

import java.time.LocalDateTime;

import com.example.ticketsystem.dto.user.UserSimpleDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private UserSimpleDto author;
}

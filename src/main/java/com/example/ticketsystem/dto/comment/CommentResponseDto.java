package com.example.ticketsystem.dto.comment;

import com.example.ticketsystem.dto.user.UserSimpleDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private UserSimpleDto author;
}

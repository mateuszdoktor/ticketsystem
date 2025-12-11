package com.example.ticketsystem.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentCreateDto {
    @NotBlank
    private String text;
    @NotNull
    private Long ticketId;
    @NotNull
    private Long authorId;
}

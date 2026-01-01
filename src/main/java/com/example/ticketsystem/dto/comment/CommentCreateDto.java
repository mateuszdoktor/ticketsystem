package com.example.ticketsystem.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentCreateDto {
    @NotBlank(message = "Comment text is required")
    @Size(min = 1, max = 300, message = "Comment must be between 1 and 300 characters")
    private String text;
}

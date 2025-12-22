package com.example.ticketsystem.dto.ticket;

import com.example.ticketsystem.entity.TicketPriority;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TicketCreateDto {
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 500)
    private String description;
    @NotNull
    private TicketPriority priority;
    @Nullable
    private Long assignedToId;
}


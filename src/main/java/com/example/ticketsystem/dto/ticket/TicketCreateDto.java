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
    private String title;
    @NotBlank
    @Size(max = 500)
    private String description;
    @NotNull
    private TicketPriority priority;
    @NotNull
    private Long createdById;
    @Nullable
    private Long assignedToId;
}


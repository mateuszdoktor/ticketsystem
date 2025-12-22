package com.example.ticketsystem.dto.ticket;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TicketAssignRequestDto {
    @NotNull
    private Long userId;
}

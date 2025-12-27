package com.example.ticketsystem.dto.ticket;

import com.example.ticketsystem.entity.ticket.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TicketStatusUpdateRequestDto {
    @NotNull
    private TicketStatus status;
}

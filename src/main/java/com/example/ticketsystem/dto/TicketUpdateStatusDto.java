package com.example.ticketsystem.dto;

import com.example.ticketsystem.entity.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TicketUpdateStatusDto {
    @NotNull
    private TicketStatus status;
}

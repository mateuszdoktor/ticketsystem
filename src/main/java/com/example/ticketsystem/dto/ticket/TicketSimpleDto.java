package com.example.ticketsystem.dto.ticket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TicketSimpleDto {
    private Long id;
    private String title;
    private String description;
}


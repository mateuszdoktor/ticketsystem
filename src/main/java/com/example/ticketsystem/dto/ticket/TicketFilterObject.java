package com.example.ticketsystem.dto.ticket;

import com.example.ticketsystem.entity.TicketPriority;
import com.example.ticketsystem.entity.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketFilterObject {
    private TicketStatus status;
    private TicketPriority priority;
    private Long createdById;
    private Long assignedToId;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
}

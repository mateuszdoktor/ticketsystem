package com.example.ticketsystem.dto.ticket;

import com.example.ticketsystem.entity.TicketPriority;
import com.example.ticketsystem.entity.TicketStatus;

import java.time.LocalDateTime;

public class TicketFilterObject {
    private TicketStatus status;
    private TicketPriority priority;
    private Long createdById;
    private Long assignedToId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}

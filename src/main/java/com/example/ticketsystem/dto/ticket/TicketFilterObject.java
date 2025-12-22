package com.example.ticketsystem.dto.ticket;

import com.example.ticketsystem.entity.TicketPriority;
import com.example.ticketsystem.entity.TicketStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketFilterObject {
    private TicketStatus status;
    private TicketPriority priority;
    private Long createdById;
    private Long assignedToId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtTo;
}

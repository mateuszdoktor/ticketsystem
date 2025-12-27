package com.example.ticketsystem.dto.ticket;

import com.example.ticketsystem.dto.user.UserSimpleDto;
import com.example.ticketsystem.entity.ticket.TicketPriority;
import com.example.ticketsystem.entity.ticket.TicketStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class TicketListDto {
    private Long id;
    private String title;
    private TicketStatus status;
    private TicketPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
    private UserSimpleDto createdBy;
    private UserSimpleDto assignedTo;
}

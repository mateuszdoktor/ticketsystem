package com.example.ticketsystem.dto.ticket;

import java.time.LocalDateTime;

import com.example.ticketsystem.dto.user.UserSimpleDto;
import com.example.ticketsystem.entity.TicketPriority;
import com.example.ticketsystem.entity.TicketStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

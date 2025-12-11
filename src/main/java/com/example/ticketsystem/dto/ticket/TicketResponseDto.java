package com.example.ticketsystem.dto.ticket;

import com.example.ticketsystem.dto.comment.CommentResponseDto;
import com.example.ticketsystem.dto.user.UserSimpleDto;
import com.example.ticketsystem.entity.TicketPriority;
import com.example.ticketsystem.entity.TicketStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class TicketResponseDto {
    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserSimpleDto createdBy;
    private UserSimpleDto assignedTo;
    private List<CommentResponseDto> comments = new ArrayList<>();
}

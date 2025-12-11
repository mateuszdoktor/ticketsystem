package com.example.ticketsystem.dto.user;

import com.example.ticketsystem.dto.comment.CommentResponseDto;
import com.example.ticketsystem.dto.ticket.TicketSimpleDto;
import com.example.ticketsystem.entity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private UserRole role;
    private List<TicketSimpleDto> createdTickets = new ArrayList<>();
    private List<TicketSimpleDto> assignedTickets = new ArrayList<>();
    private List<CommentResponseDto> comments = new ArrayList<>();
}

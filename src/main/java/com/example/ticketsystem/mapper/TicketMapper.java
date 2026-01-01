package com.example.ticketsystem.mapper;

import org.mapstruct.Mapper;

import com.example.ticketsystem.dto.ticket.TicketListDto;
import com.example.ticketsystem.dto.ticket.TicketResponseDto;
import com.example.ticketsystem.dto.ticket.TicketSimpleDto;
import com.example.ticketsystem.entity.ticket.Ticket;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface TicketMapper {
    TicketSimpleDto toSimpleDto(Ticket ticket);

    TicketListDto toListDto(Ticket ticket);

    TicketResponseDto toResponseDto(Ticket ticket);
}

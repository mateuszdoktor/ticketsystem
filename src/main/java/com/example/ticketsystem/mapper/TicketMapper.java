package com.example.ticketsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ticketsystem.dto.ticket.TicketCreateDto;
import com.example.ticketsystem.dto.ticket.TicketListDto;
import com.example.ticketsystem.dto.ticket.TicketResponseDto;
import com.example.ticketsystem.dto.ticket.TicketSimpleDto;
import com.example.ticketsystem.entity.Ticket;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface TicketMapper {
    TicketSimpleDto toSimpleDto(Ticket ticket);

    TicketListDto toListDto(Ticket ticket);

    TicketResponseDto toResponseDto(Ticket ticket);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Ticket toEntity(TicketCreateDto ticketCreateDto);
}

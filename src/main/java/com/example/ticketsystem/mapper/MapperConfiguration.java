package com.example.ticketsystem.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.example.ticketsystem.dto.comment.CommentCreateDto;
import com.example.ticketsystem.dto.comment.CommentResponseDto;
import com.example.ticketsystem.dto.ticket.TicketListDto;
import com.example.ticketsystem.dto.ticket.TicketResponseDto;
import com.example.ticketsystem.dto.ticket.TicketSimpleDto;
import com.example.ticketsystem.dto.user.UserResponseDto;
import com.example.ticketsystem.dto.user.UserSimpleDto;
import com.example.ticketsystem.entity.comment.Comment;
import com.example.ticketsystem.entity.ticket.Ticket;
import com.example.ticketsystem.entity.user.User;

@Configuration
public class MapperConfiguration {

    @Bean
    @Primary
    public UserMapper userMapper() {
        return new UserMapper() {
            @Override
            public UserSimpleDto toSimpleDto(User user) {
                if (user == null) {
                    return null;
                }

                UserSimpleDto dto = new UserSimpleDto();
                dto.setId(user.getId());
                dto.setUsername(user.getUsername());
                return dto;
            }

            @Override
            public UserResponseDto toResponseDto(User user) {
                if (user == null) {
                    return null;
                }

                UserResponseDto dto = new UserResponseDto();
                dto.setId(user.getId());
                dto.setUsername(user.getUsername());
                dto.setRole(user.getUserRole());
                return dto;
            }
        };
    }

    @Bean
    @Primary
    public CommentMapper commentMapper(UserMapper userMapper) {
        return new CommentMapper() {
            @Override
            public Comment toEntity(CommentCreateDto commentCreateDto) {
                if (commentCreateDto == null) {
                    return null;
                }

                Comment comment = new Comment();
                comment.setText(commentCreateDto.getText());
                return comment;
            }

            @Override
            public CommentResponseDto toResponseDto(Comment comment) {
                if (comment == null) {
                    return null;
                }

                CommentResponseDto dto = new CommentResponseDto();
                dto.setId(comment.getId());
                dto.setText(comment.getText());
                dto.setCreatedAt(comment.getCreatedAt());
                dto.setAuthor(userMapper.toSimpleDto(comment.getAuthor()));
                return dto;
            }
        };
    }

    @Bean
    @Primary
    public TicketMapper ticketMapper(UserMapper userMapper, CommentMapper commentMapper) {
        return new TicketMapper() {
            @Override
            public TicketSimpleDto toSimpleDto(Ticket ticket) {
                if (ticket == null) {
                    return null;
                }

                TicketSimpleDto dto = new TicketSimpleDto();
                dto.setId(ticket.getId());
                dto.setTitle(ticket.getTitle());
                return dto;
            }

            @Override
            public TicketListDto toListDto(Ticket ticket) {
                if (ticket == null) {
                    return null;
                }

                TicketListDto dto = new TicketListDto();
                dto.setId(ticket.getId());
                dto.setTitle(ticket.getTitle());
                dto.setStatus(ticket.getStatus());
                dto.setPriority(ticket.getPriority());
                dto.setCreatedAt(ticket.getCreatedAt());
                dto.setUpdatedAt(ticket.getUpdatedAt());
                dto.setClosedAt(ticket.getClosedAt());
                dto.setCreatedBy(userMapper.toSimpleDto(ticket.getCreatedBy()));
                dto.setAssignedTo(userMapper.toSimpleDto(ticket.getAssignedTo()));
                return dto;
            }

            @Override
            public TicketResponseDto toResponseDto(Ticket ticket) {
                if (ticket == null) {
                    return null;
                }

                TicketResponseDto dto = new TicketResponseDto();
                dto.setId(ticket.getId());
                dto.setTitle(ticket.getTitle());
                dto.setDescription(ticket.getDescription());
                dto.setStatus(ticket.getStatus());
                dto.setPriority(ticket.getPriority());
                dto.setCreatedAt(ticket.getCreatedAt());
                dto.setUpdatedAt(ticket.getUpdatedAt());
                dto.setClosedAt(ticket.getClosedAt());
                dto.setCreatedBy(userMapper.toSimpleDto(ticket.getCreatedBy()));
                dto.setAssignedTo(userMapper.toSimpleDto(ticket.getAssignedTo()));

                List<CommentResponseDto> comments = new ArrayList<>();
                if (ticket.getComments() != null) {
                    for (Comment comment : ticket.getComments()) {
                        comments.add(commentMapper.toResponseDto(comment));
                    }
                }
                dto.setComments(comments);

                return dto;
            }
        };
    }
}
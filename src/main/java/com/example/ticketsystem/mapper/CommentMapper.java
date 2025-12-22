package com.example.ticketsystem.mapper;

import com.example.ticketsystem.dto.comment.CommentCreateDto;
import com.example.ticketsystem.dto.comment.CommentResponseDto;
import com.example.ticketsystem.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment toEntity(CommentCreateDto commentCreateDto);

    CommentResponseDto toResponseDto(Comment comment);
}

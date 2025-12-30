package com.example.ticketsystem.service;

import com.example.ticketsystem.entity.comment.Comment;
import com.example.ticketsystem.exceptions.ticket.TicketNotFoundException;
import com.example.ticketsystem.exceptions.user.UserNotFoundException;
import com.example.ticketsystem.repository.CommentRepository;
import com.example.ticketsystem.repository.TicketRepository;
import com.example.ticketsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/*
 *  CommentService: CommentService.java
 *      findByTicketId: 404 gdy ticket nie istnieje; w przeciwnym razie delegacja repo.
 *      findByAuthorId: 404 gdy user nie istnieje; delegacja repo.
 *
 * */

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    Page<Comment> repoPage = null;
    Pageable pageable = null;
    Long id = null;

    @Mock
    CommentRepository commentRepository;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    CommentService commentService;

    @BeforeEach
    void setup() {
        repoPage = new PageImpl<>(List.of(new Comment()));
        pageable = PageRequest.of(0, 10);
        id = 1L;
    }

    @Nested
    class WhenFindingByTicketId {

        @Test
        void findByTicketId_TicketDoesNotExist_ShouldThrowTicketNotFoundException() {
            given(ticketRepository.existsById(id)).willReturn(false);
            assertThrows(TicketNotFoundException.class, () -> commentService.findByTicketId(id, pageable));
        }

        @Test
        void findByTicketId_ShouldDelegateToRepositoryAndReturnPage() {
            given(ticketRepository.existsById(id)).willReturn(true);
            given(commentRepository.findByTicketId(id, pageable)).willReturn(repoPage);

            Page<Comment> result = commentService.findByTicketId(id, pageable);

            assertSame(repoPage, result);

            then(commentRepository).should().findByTicketId(id, pageable);
        }
    }

    @Nested
    class WhenFindingByUserId {

        @Test
        void findByAuthorId_UserDoesNotExist_ShouldThrowUserNotFoundException() {
            given(userRepository.existsById(id)).willReturn(false);
            assertThrows(UserNotFoundException.class, () -> commentService.findByAuthorId(id, pageable));
        }

        @Test
        void findByAuthorId_ShouldDelegateToRepositoryAndReturnPage() {
            given(userRepository.existsById(id)).willReturn(true);
            given(commentRepository.findByAuthorId(id, pageable)).willReturn(repoPage);

            Page<Comment> result = commentService.findByAuthorId(id, pageable);

            assertSame(repoPage, result);

            then(commentRepository).should().findByAuthorId(id, pageable);
        }
    }
}

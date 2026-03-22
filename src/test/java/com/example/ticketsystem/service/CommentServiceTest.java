package com.example.ticketsystem.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.ticketsystem.entity.comment.Comment;
import com.example.ticketsystem.entity.ticket.Ticket;
import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.entity.user.UserRole;
import com.example.ticketsystem.exceptions.ticket.TicketNotFoundException;
import com.example.ticketsystem.exceptions.user.UserNotFoundException;
import com.example.ticketsystem.repository.CommentRepository;
import com.example.ticketsystem.repository.TicketRepository;
import com.example.ticketsystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    @Nested
    class WhenFindingByTicketId {

        private Pageable pageable;
        private Long ticketId;
        private User loggedUser;

        @BeforeEach
        void setup() {
            pageable = PageRequest.of(0, 10);
            ticketId = 1L;
            loggedUser = new User();
            loggedUser.setUserRole(UserRole.ROLE_ADMIN);
        }

        @Test
        void findByTicketId_WhenTicketDoesNotExist_ShouldThrowTicketNotFoundException() {
            given(ticketRepository.findById(ticketId)).willReturn(Optional.empty());

            assertThrows(TicketNotFoundException.class, () -> commentService.findByTicketId(ticketId, loggedUser, pageable));
        }

        @Test
        void findByTicketId_ShouldDelegateToRepositoryAndReturnPage() {
            Ticket ticket = new Ticket();
            Page<Comment> expectedPage = new PageImpl<>(List.of(new Comment()));
            given(ticketRepository.findById(ticketId)).willReturn(Optional.of(ticket));
            given(commentRepository.findByTicketId(ticketId, pageable)).willReturn(expectedPage);

            Page<Comment> result = commentService.findByTicketId(ticketId, loggedUser, pageable);

            assertSame(expectedPage, result);
            then(commentRepository).should().findByTicketId(ticketId, pageable);
        }
    }

    @Nested
    class WhenFindingByUserId {

        private Pageable pageable;
        private Long userId;

        @BeforeEach
        void setup() {
            pageable = PageRequest.of(0, 10);
            userId = 1L;
        }

        @Test
        void findByAuthorId_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() {
            given(userRepository.existsById(userId)).willReturn(false);

            assertThrows(UserNotFoundException.class, () -> commentService.findByAuthorId(userId, pageable));
        }

        @Test
        void findByAuthorId_ShouldDelegateToRepositoryAndReturnPage() {
            Page<Comment> expectedPage = new PageImpl<>(List.of(new Comment()));
            given(userRepository.existsById(userId)).willReturn(true);
            given(commentRepository.findByAuthorId(userId, pageable)).willReturn(expectedPage);

            Page<Comment> result = commentService.findByAuthorId(userId, pageable);

            assertSame(expectedPage, result);
            then(commentRepository).should().findByAuthorId(userId, pageable);
        }
    }
}

package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.comment.CommentCreateDto;
import com.example.ticketsystem.dto.ticket.TicketCreateDto;
import com.example.ticketsystem.entity.comment.Comment;
import com.example.ticketsystem.entity.ticket.Ticket;
import com.example.ticketsystem.entity.ticket.TicketPriority;
import com.example.ticketsystem.entity.ticket.TicketStatus;
import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.entity.user.UserRole;
import com.example.ticketsystem.exceptions.security.AccessDeniedException;
import com.example.ticketsystem.exceptions.ticket.TicketNotFoundException;
import com.example.ticketsystem.exceptions.user.UserNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @Mock
    TicketRepository ticketRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TicketService ticketService;

    User user = null;
    User user1 = null;
    Pageable pageable = null;
    Ticket ticket = null;

    @Nested
    class WhenCreatingTicket {

        TicketCreateDto ticketCreateDto;

        @BeforeEach
        void setup() {
            ticketCreateDto = new TicketCreateDto();
            user = new User();
        }

        @Test
        void createTicket_ShouldAssignStatusNew() {
            given(ticketRepository.save(any(Ticket.class))).willAnswer((invocation) -> invocation.getArgument(0));

            Ticket result = ticketService.createTicket(ticketCreateDto, user);

            assertEquals(TicketStatus.NEW, result.getStatus());
        }

        @Test
        void createTicket_ShouldAssignPriorityFromDto() {
            ticketCreateDto.setPriority(TicketPriority.HIGH);
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.createTicket(ticketCreateDto, user);

            assertEquals(ticketCreateDto.getPriority(), result.getPriority());
        }

        @Test
        void createTicket_ShouldAssignTicketToUser() {
            user.setId(1L);
            ticketCreateDto.setAssignedToId(1L);

            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.createTicket(ticketCreateDto, user);

            assertEquals(user, result.getAssignedTo());
        }

        @Test
        void createTicket_UserDoesNotExist_ShouldThrowUserNotFoundException() {
            ticketCreateDto.setAssignedToId(1L);
            given(userRepository.findById(1L)).willReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> ticketService.createTicket(ticketCreateDto, user));
        }

        @Test
        void createTicket_ShouldReturnSavedTicket() {
            Ticket savedTicket = new Ticket();
            savedTicket.setId(1L);
            savedTicket.setStatus(TicketStatus.NEW);

            given(ticketRepository.save(any(Ticket.class))).willReturn(savedTicket);


            assertSame(savedTicket, ticketService.createTicket(ticketCreateDto, user));
        }
    }

    @Nested
    class WhenFindingById {
        @Test
        void findById_ShouldDelegateToRepositoryFindDetailedById() {
            ticket = new Ticket();
            given(ticketRepository.findDetailedById(1L)).willReturn(Optional.of(ticket));

            assertSame(ticket, ticketService.findById(1L));

        }

        @Test
        void findById_TicketDoesNotExist_ShouldThrowTicketNotFoundException() {
            given(ticketRepository.findDetailedById(1L)).willReturn(Optional.empty());

            assertThrows(TicketNotFoundException.class, () -> ticketService.findById(1L));
        }
    }

    @Nested
    class WhenFindingByCreatedById {
        @BeforeEach
        void setup() {
            pageable = PageRequest.of(0, 10);
        }

        @Test
        void findByCreatedById_ShouldDelegateToRepositoryAndReturnPage() {
            Page<Ticket> repoPage = new PageImpl<>(List.of(new Ticket()));
            given(userRepository.existsById(1L)).willReturn(true);
            given(ticketRepository.findByCreatedById(1L, pageable)).willReturn(repoPage);

            assertSame(repoPage, ticketService.findByCreatedById(1L, pageable));
        }

        @Test
        void findByCreatedById_UserDoesNotExist_ShouldThrowUserNotFoundException() {
            given(userRepository.existsById(1L)).willReturn(false);

            assertThrows(UserNotFoundException.class, () -> ticketService.findByCreatedById(1L, pageable));
        }
    }

    @Nested
    class WhenFindingByAssignedToId {
        @BeforeEach
        void setup() {
            pageable = PageRequest.of(0, 10);
        }

        @Test
        void findByAssignedToId_ShouldDelegateToRepositoryAndReturnPage() {
            Page<Ticket> repoPage = new PageImpl<>(List.of(new Ticket()));
            given(userRepository.existsById(1L)).willReturn(true);
            given(ticketRepository.findByAssignedToId(1L, pageable)).willReturn(repoPage);

            assertSame(repoPage, ticketService.findByAssignedToId(1L, pageable));
        }

        @Test
        void findByAssignedToId_UserDoesNotExist_ShouldThrowUserNotFoundException() {
            given(userRepository.existsById(1L)).willReturn(false);

            assertThrows(UserNotFoundException.class, () -> ticketService.findByAssignedToId(1L, pageable));
        }

    }

    @Nested
    class WhenAssigningTicket {
        @BeforeEach
        void setup() {
            user = new User();
        }

        @Test
        void assignTicket_WhenUserNotAuthorized_ShouldThrowAccessDeniedException() {
            user.setUserRole(UserRole.ROLE_USER);
            Ticket ticket = new Ticket();
            User user1 = new User();
            user1.setId(1L);
            ticket.setCreatedBy(user1);
            given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));


            assertThrows(AccessDeniedException.class, () -> ticketService.assignTicket(1L, 1L, user));
        }

        @Test
        void assignTicket_UserDoesNotExist_ShouldThrowUserNotFoundException() {
            given(ticketRepository.findById(1L)).willReturn(Optional.of(new Ticket()));
            user.setUserRole(UserRole.ROLE_ADMIN);
            given(userRepository.findById(1L)).willReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> ticketService.assignTicket(1L, 1L, user));
        }

        @Test
        void assignTicket_ShouldAssignAndReturnTicket() {
            Ticket ticket = new Ticket();
            ticket.setId(1L);
            ticket.setCreatedBy(new User());

            user.setUserRole(UserRole.ROLE_ADMIN);

            User user1 = new User();
            user1.setId(2L);
            given(userRepository.findById(2L)).willReturn(Optional.of(user1));
            given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.assignTicket(1L, 2L, user);

            assertSame(user1, result.getAssignedTo());
        }

        @Test
        void assignTicket_TicketDoesNotExist_ShouldThrowTicketNotFoundException() {
            given(ticketRepository.findById(any(Long.class))).willReturn(Optional.empty());

            assertThrows(TicketNotFoundException.class, () -> ticketService.assignTicket(1L, 1L, user));
        }
    }

    @Nested
    class WhenChangingStatus {
        @BeforeEach
        void setup() {
            user = new User();
            user.setId(1L);

            user1 = new User();
            user1.setId(2L);

            ticket = new Ticket();
            ticket.setCreatedBy(user1);
            ticket.setStatus(TicketStatus.NEW);

            given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));
        }

        @Test
        void changeStatus_TicketDoesNotExist_ShouldThrowTicketNotFoundException() {
            given(ticketRepository.findById(1L)).willReturn(Optional.empty());

            assertThrows(TicketNotFoundException.class, () -> ticketService.changeStatus(1L, TicketStatus.IN_PROGRESS, user));
        }

        @Test
        void changeStatus_UserNotAuthorized_ShouldThrowAccessDeniedException() {
            user.setUserRole(UserRole.ROLE_USER);

            assertThrows(AccessDeniedException.class, () -> ticketService.changeStatus(1L, TicketStatus.IN_PROGRESS, user));
        }

        @Test
        void changeStatus_IllegalTransition_ShouldThrowIllegalArgumentException() {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            user.setUserRole(UserRole.ROLE_ADMIN);

            assertThrows(IllegalArgumentException.class, () -> ticketService.changeStatus(1L, TicketStatus.NEW, user));
        }

        @Test
        void changeStatus_StatusChangedToClosed_ShouldSetClosedAtProperty() {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            user.setUserRole(UserRole.ROLE_ADMIN);
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            assertNotNull(ticketService.changeStatus(1L, TicketStatus.CLOSED, user).getClosedAt());
        }

        @Test
        void changeStatus_StatusChangedToInProgress_ShouldNotSetClosedAtProperty() {
            ticket.setStatus(TicketStatus.NEW);
            user.setUserRole(UserRole.ROLE_ADMIN);
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            assertNull(ticketService.changeStatus(1L, TicketStatus.IN_PROGRESS, user).getClosedAt());
        }
    }

    @Nested
    class WhenAddingComment {

        CommentCreateDto commentCreateDto;

        @BeforeEach
        void setup() {
            user = new User();
            user.setId(1L);

            user1 = new User();
            user1.setId(2L);

            ticket = new Ticket();
            ticket.setCreatedBy(user1);
            ticket.setStatus(TicketStatus.NEW);

            commentCreateDto = new CommentCreateDto();

            given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));
        }

        @Test
        void addComment_TicketDoesNotExist_ShouldThrowTicketNotFoundException() {
            given(ticketRepository.findById(1L)).willReturn(Optional.empty());

            assertThrows(TicketNotFoundException.class, () -> ticketService.addComment(1L, commentCreateDto, user));
        }

        @Test
        void addComment_AssignsAuthorAndSetRelationshipWithTicket() {
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));
            Comment result = ticketService.addComment(1L, commentCreateDto, user);

            assertSame(user, result.getAuthor());
            assertSame(ticket, result.getTicket());
            assertTrue(ticket.getComments().contains(result));
        }
    }
}

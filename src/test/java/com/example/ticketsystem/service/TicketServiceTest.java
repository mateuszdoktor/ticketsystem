package com.example.ticketsystem.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketService ticketService;

    @Nested
    class WhenCreatingTicket {

        private TicketCreateDto ticketCreateDto;
        private User creator;

        @BeforeEach
        void setup() {
            ticketCreateDto = new TicketCreateDto();
            creator = new User();
        }

        @Test
        void createTicket_ShouldAssignStatusNew() {
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.createTicket(ticketCreateDto, creator);

            assertEquals(TicketStatus.NEW, result.getStatus());
        }

        @Test
        void createTicket_ShouldAssignPriorityFromDto() {
            ticketCreateDto.setPriority(TicketPriority.HIGH);
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.createTicket(ticketCreateDto, creator);

            assertEquals(TicketPriority.HIGH, result.getPriority());
        }

        @Test
        void createTicket_WhenAssignedToIdProvided_ShouldAssignTicketToUser() {
            Long assigneeId = 1L;
            User assignee = new User();
            assignee.setId(assigneeId);
            ticketCreateDto.setAssignedToId(assigneeId);

            given(userRepository.findById(assigneeId)).willReturn(Optional.of(assignee));
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.createTicket(ticketCreateDto, creator);

            assertEquals(assignee, result.getAssignedTo());
        }

        @Test
        void createTicket_WhenAssignedUserDoesNotExist_ShouldThrowUserNotFoundException() {
            Long nonExistentUserId = 999L;
            ticketCreateDto.setAssignedToId(nonExistentUserId);
            given(userRepository.findById(nonExistentUserId)).willReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> ticketService.createTicket(ticketCreateDto, creator));
        }

        @Test
        void createTicket_ShouldReturnSavedTicket() {
            Ticket savedTicket = new Ticket();
            savedTicket.setId(1L);
            savedTicket.setStatus(TicketStatus.NEW);
            given(ticketRepository.save(any(Ticket.class))).willReturn(savedTicket);

            Ticket result = ticketService.createTicket(ticketCreateDto, creator);

            assertSame(savedTicket, result);
        }
    }

    @Nested
    class WhenFindingById {

        @Test
        void findById_ShouldDelegateToRepositoryFindDetailedById() {
            Long ticketId = 1L;
            Ticket expectedTicket = new Ticket();
            given(ticketRepository.findDetailedById(ticketId)).willReturn(Optional.of(expectedTicket));

            Ticket result = ticketService.findById(ticketId);

            assertSame(expectedTicket, result);
        }

        @Test
        void findById_WhenTicketDoesNotExist_ShouldThrowTicketNotFoundException() {
            Long ticketId = 1L;
            given(ticketRepository.findDetailedById(ticketId)).willReturn(Optional.empty());

            assertThrows(TicketNotFoundException.class, () -> ticketService.findById(ticketId));
        }
    }

    @Nested
    class WhenFindingByCreatedById {

        private Pageable pageable;

        @BeforeEach
        void setup() {
            pageable = PageRequest.of(0, 10);
        }

        @Test
        void findByCreatedById_ShouldDelegateToRepositoryAndReturnPage() {
            Long userId = 1L;
            Page<Ticket> expectedPage = new PageImpl<>(List.of(new Ticket()));
            given(userRepository.existsById(userId)).willReturn(true);
            given(ticketRepository.findByCreatedById(userId, pageable)).willReturn(expectedPage);

            Page<Ticket> result = ticketService.findByCreatedById(userId, pageable);

            assertSame(expectedPage, result);
        }

        @Test
        void findByCreatedById_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() {
            Long userId = 1L;
            given(userRepository.existsById(userId)).willReturn(false);

            assertThrows(UserNotFoundException.class, () -> ticketService.findByCreatedById(userId, pageable));
        }
    }

    @Nested
    class WhenFindingByAssignedToId {

        private Pageable pageable;

        @BeforeEach
        void setup() {
            pageable = PageRequest.of(0, 10);
        }

        @Test
        void findByAssignedToId_ShouldDelegateToRepositoryAndReturnPage() {
            Long userId = 1L;
            Page<Ticket> expectedPage = new PageImpl<>(List.of(new Ticket()));
            given(userRepository.existsById(userId)).willReturn(true);
            given(ticketRepository.findByAssignedToId(userId, pageable)).willReturn(expectedPage);

            Page<Ticket> result = ticketService.findByAssignedToId(userId, pageable);

            assertSame(expectedPage, result);
        }

        @Test
        void findByAssignedToId_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() {
            Long userId = 1L;
            given(userRepository.existsById(userId)).willReturn(false);

            assertThrows(UserNotFoundException.class, () -> ticketService.findByAssignedToId(userId, pageable));
        }
    }

    @Nested
    class WhenAssigningTicket {

        private User requestingUser;

        @BeforeEach
        void setup() {
            requestingUser = new User();
        }

        @Test
        void assignTicket_WhenUserNotAuthorized_ShouldThrowAccessDeniedException() {
            Long ticketId = 1L;
            Long assigneeId = 1L;
            requestingUser.setUserRole(UserRole.ROLE_USER);
            
            Ticket ticket = new Ticket();
            User ticketCreator = new User();
            ticketCreator.setId(2L);
            ticket.setCreatedBy(ticketCreator);
            
            given(ticketRepository.findById(ticketId)).willReturn(Optional.of(ticket));

            assertThrows(AccessDeniedException.class, () -> ticketService.assignTicket(ticketId, assigneeId, requestingUser));
        }

        @Test
        void assignTicket_WhenAssigneeDoesNotExist_ShouldThrowUserNotFoundException() {
            Long ticketId = 1L;
            Long assigneeId = 1L;
            requestingUser.setUserRole(UserRole.ROLE_ADMIN);
            
            given(ticketRepository.findById(ticketId)).willReturn(Optional.of(new Ticket()));
            given(userRepository.findById(assigneeId)).willReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> ticketService.assignTicket(ticketId, assigneeId, requestingUser));
        }

        @Test
        void assignTicket_WhenAuthorized_ShouldAssignAndReturnTicket() {
            Long ticketId = 1L;
            Long assigneeId = 2L;
            
            Ticket ticket = new Ticket();
            ticket.setId(ticketId);
            ticket.setCreatedBy(new User());

            requestingUser.setUserRole(UserRole.ROLE_ADMIN);

            User assignee = new User();
            assignee.setId(assigneeId);
            
            given(userRepository.findById(assigneeId)).willReturn(Optional.of(assignee));
            given(ticketRepository.findById(ticketId)).willReturn(Optional.of(ticket));
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.assignTicket(ticketId, assigneeId, requestingUser);

            assertSame(assignee, result.getAssignedTo());
        }

        @Test
        void assignTicket_WhenTicketDoesNotExist_ShouldThrowTicketNotFoundException() {
            Long ticketId = 1L;
            Long assigneeId = 1L;
            given(ticketRepository.findById(ticketId)).willReturn(Optional.empty());

            assertThrows(TicketNotFoundException.class, () -> ticketService.assignTicket(ticketId, assigneeId, requestingUser));
        }
    }

    @Nested
    class WhenChangingStatus {

        private User requestingUser;
        private User ticketCreator;
        private Ticket ticket;
        private Long ticketId;

        @BeforeEach
        void setup() {
            ticketId = 1L;
            
            requestingUser = new User();
            requestingUser.setId(1L);

            ticketCreator = new User();
            ticketCreator.setId(2L);

            ticket = new Ticket();
            ticket.setCreatedBy(ticketCreator);
            ticket.setStatus(TicketStatus.NEW);

            given(ticketRepository.findById(ticketId)).willReturn(Optional.of(ticket));
        }

        @Test
        void changeStatus_WhenTicketDoesNotExist_ShouldThrowTicketNotFoundException() {
            given(ticketRepository.findById(ticketId)).willReturn(Optional.empty());

            assertThrows(TicketNotFoundException.class, () -> ticketService.changeStatus(ticketId, TicketStatus.IN_PROGRESS, requestingUser));
        }

        @Test
        void changeStatus_WhenUserNotAuthorized_ShouldThrowAccessDeniedException() {
            requestingUser.setUserRole(UserRole.ROLE_USER);

            assertThrows(AccessDeniedException.class, () -> ticketService.changeStatus(ticketId, TicketStatus.IN_PROGRESS, requestingUser));
        }

        @Test
        void changeStatus_WhenIllegalTransition_ShouldThrowIllegalArgumentException() {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            requestingUser.setUserRole(UserRole.ROLE_ADMIN);

            assertThrows(IllegalArgumentException.class, () -> ticketService.changeStatus(ticketId, TicketStatus.NEW, requestingUser));
        }

        @Test
        void changeStatus_WhenChangedToClosed_ShouldSetClosedAtProperty() {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            requestingUser.setUserRole(UserRole.ROLE_ADMIN);
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.changeStatus(ticketId, TicketStatus.CLOSED, requestingUser);

            assertNotNull(result.getClosedAt());
        }

        @Test
        void changeStatus_WhenChangedToInProgress_ShouldNotSetClosedAtProperty() {
            ticket.setStatus(TicketStatus.NEW);
            requestingUser.setUserRole(UserRole.ROLE_ADMIN);
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));

            Ticket result = ticketService.changeStatus(ticketId, TicketStatus.IN_PROGRESS, requestingUser);

            assertNull(result.getClosedAt());
        }
    }

    @Nested
    class WhenAddingComment {

        private CommentCreateDto commentCreateDto;
        private User commentAuthor;
        private User ticketCreator;
        private Ticket ticket;
        private Long ticketId;

        @BeforeEach
        void setup() {
            ticketId = 1L;
            
            commentAuthor = new User();
            commentAuthor.setId(1L);

            ticketCreator = new User();
            ticketCreator.setId(2L);

            ticket = new Ticket();
            ticket.setCreatedBy(ticketCreator);
            ticket.setStatus(TicketStatus.NEW);

            commentCreateDto = new CommentCreateDto();

            given(ticketRepository.findById(ticketId)).willReturn(Optional.of(ticket));
        }

        @Test
        void addComment_WhenTicketDoesNotExist_ShouldThrowTicketNotFoundException() {
            given(ticketRepository.findById(ticketId)).willReturn(Optional.empty());

            assertThrows(TicketNotFoundException.class, () -> ticketService.addComment(ticketId, commentCreateDto, commentAuthor));
        }

        @Test
        void addComment_ShouldAssignAuthorAndSetRelationshipWithTicket() {
            given(ticketRepository.save(any(Ticket.class))).willAnswer(invocation -> invocation.getArgument(0));
            
            Comment result = ticketService.addComment(ticketId, commentCreateDto, commentAuthor);

            assertSame(commentAuthor, result.getAuthor());
            assertSame(ticket, result.getTicket());
            assertTrue(ticket.getComments().contains(result));
        }
    }
}

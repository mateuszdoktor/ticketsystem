package com.example.ticketsystem.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.ticketsystem.dto.ticket.TicketFilterObject;
import com.example.ticketsystem.entity.ticket.Ticket;
import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.repository.TicketRepository;
import com.example.ticketsystem.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TicketServiceMethodSecurityTest.MethodSecurityTestConfig.class)
class TicketServiceMethodSecurityTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @SuppressWarnings("unused")
    void resetMocks() {
        Mockito.reset(ticketRepository);
        Mockito.reset(userRepository);
    }

    @Test
    @WithMockUser(roles = "USER")
    void searchTickets_WhenRoleUser_ShouldDenyAccess() {
        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> ticketService.searchTickets(new TicketFilterObject(), PageRequest.of(0, 10)));
        assertNotNull(exception);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void searchTickets_WhenRoleManager_ShouldAllowAccess() {
        when(ticketRepository.findAll(anyTicketSpecification(), any(Pageable.class)))
                .thenReturn(Page.empty());

        assertDoesNotThrow(() -> ticketService.searchTickets(new TicketFilterObject(), PageRequest.of(0, 10)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void assignTicket_WhenRoleUser_ShouldDenyAccess() {
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> ticketService.assignTicket(1L, 2L));
        assertNotNull(exception);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void assignTicket_WhenRoleManager_ShouldAllowAccess() {
        Ticket ticket = new Ticket();
        User assignee = new User();

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        assertDoesNotThrow(() -> ticketService.assignTicket(1L, 2L));
    }

    @SuppressWarnings("unchecked")
    private Specification<Ticket> anyTicketSpecification() {
        return any(Specification.class);
    }

    @Configuration
    @EnableMethodSecurity
    @SuppressWarnings("unused")
    static class MethodSecurityTestConfig {

        @Bean
        TicketRepository ticketRepository() {
            return Mockito.mock(TicketRepository.class);
        }

        @Bean
        UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }

        @Bean
        TicketService ticketService(TicketRepository ticketRepository, UserRepository userRepository) {
            return new TicketService(ticketRepository, userRepository);
        }
    }
}

package com.example.ticketsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.example.ticketsystem.entity.ticket.Ticket;
import com.example.ticketsystem.entity.ticket.TicketPriority;
import com.example.ticketsystem.entity.ticket.TicketStatus;
import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.entity.user.UserRole;
import com.example.ticketsystem.repository.TicketRepository;
import com.example.ticketsystem.repository.UserRepository;
import com.example.ticketsystem.security.JwtService;
import com.example.ticketsystem.security.MyUserDetails;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void getTicket_ShouldReturn200_WhenRequesterIsCreator() throws Exception {
        User creator = persistUser("ticket-creator", UserRole.ROLE_USER);
        Ticket ticket = persistTicket("Creator ticket", creator, null);

        mockMvc.perform(get("/api/tickets/{id}", ticket.getId())
                        .header("Authorization", bearerToken(creator)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticket.getId()));
    }

    @Test
    void getMyTickets_ShouldReturn401_WhenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/api/tickets/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getFilteredTickets_ShouldReturn403_WhenRoleUserRequestsGlobalList() throws Exception {
        User user = persistUser("ticket-user", UserRole.ROLE_USER);

        mockMvc.perform(get("/api/tickets")
                        .header("Authorization", bearerToken(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTicket_ShouldReturn404_WhenTicketDoesNotExist() throws Exception {
        User manager = persistUser("ticket-manager", UserRole.ROLE_MANAGER);

        mockMvc.perform(get("/api/tickets/{id}", 999999L)
                        .header("Authorization", bearerToken(manager)))
                .andExpect(status().isNotFound());
    }

    private String bearerToken(User user) {
        return "Bearer " + jwtService.generateToken(new MyUserDetails(user));
    }

    private User persistUser(String username, UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setUserRole(role);
        return userRepository.save(user);
    }

    private Ticket persistTicket(String title, User createdBy, User assignedTo) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription("Description for " + title);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setStatus(TicketStatus.NEW);
        ticket.setCreatedBy(createdBy);
        ticket.setAssignedTo(assignedTo);
        return ticketRepository.save(ticket);
    }
}

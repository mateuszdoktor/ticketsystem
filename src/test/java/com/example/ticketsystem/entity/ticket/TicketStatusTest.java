package com.example.ticketsystem.entity.ticket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketStatusTest {

    @Test
    void canTransitionTo_NewToInProgress_ShouldReturnTrue() {
        assertTrue(TicketStatus.NEW.canTransitionTo(TicketStatus.IN_PROGRESS));
    }
    
    @Test
    void canTransitionTo_InProgressToClosed_ShouldReturnTrue() {
        assertTrue(TicketStatus.IN_PROGRESS.canTransitionTo(TicketStatus.CLOSED));
    }

    @Test
    void canTransitionTo_InProgressToNew_ShouldReturnFalse() {
        assertFalse(TicketStatus.IN_PROGRESS.canTransitionTo(TicketStatus.NEW));
    }

    @Test
    void canTransitionTo_ClosedToInProgress_ShouldReturnFalse() {
        assertFalse(TicketStatus.CLOSED.canTransitionTo(TicketStatus.IN_PROGRESS));
    }
}

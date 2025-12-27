package com.example.ticketsystem.entity.ticket;

import java.util.EnumSet;

public enum TicketStatus {
    NEW, IN_PROGRESS, CLOSED;

    public boolean canTransitionTo(TicketStatus newStatus) {
        return switch (this) {
            case NEW -> EnumSet.of(IN_PROGRESS, CLOSED).contains(newStatus);
            case IN_PROGRESS -> EnumSet.of(CLOSED).contains(newStatus);
            case CLOSED -> false;
        };
    }
}

package com.mk.workshop.testing.confo.domain;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class TicketAvailability {
    private final ConferenceId conferenceId;
    private final Map<String, Integer> availableTicketsQuantity;

    public boolean isAvailable(Ticket ticket) {
        return availableTicketsQuantity.get(ticket.getType()) >= ticket.getQuantity();
    }

    public void decrementQuantity(Ticket ticket) {
        Integer quantity = availableTicketsQuantity.get(ticket.getType());
        availableTicketsQuantity.put(ticket.getType(), quantity - ticket.getQuantity());
    }

    public void incrementQuantity(Ticket ticket) {
        Integer quantity = availableTicketsQuantity.get(ticket.getType());
        availableTicketsQuantity.put(ticket.getType(), quantity + ticket.getQuantity());
    }
}

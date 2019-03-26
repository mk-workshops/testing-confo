package com.mk.workshop.testing.confo.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

public class Reservation {
    private final ReservationId id;
    private final Set<Ticket> tickets;
    private boolean closed = false;
    private BigDecimal totalCost;
    private LocalDateTime createDate;

    public Reservation(ReservationId id, Set<Ticket> tickets, boolean closed, BigDecimal totalCost) {
        this.id = id;
        this.tickets = tickets;
        this.closed = closed;
        this.totalCost = totalCost;
        this.createDate = LocalDateTime.now();
    }

    public Set<Ticket> getTickets() {
        return Collections.unmodifiableSet(tickets);
    }

    public ReservationId getId() {
        return id;
    }

    public void markAsClosed(BigDecimal totalCost) {
        closed = true;
        this.totalCost = totalCost;
    }

    public boolean isClosed() {
        return closed;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}

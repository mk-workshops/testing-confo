package com.mk.workshop.testing.confo.domain;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class Conference {
    private final ConferenceId id;
    private final TicketAvailability ticketAvailability;
    private final Map<ReservationId, Reservation> reservations;
    private final Map<ReservationId, Reservation> waitList;

    public Map<ReservationId, Reservation> getReservations() {
        return Collections.unmodifiableMap(reservations);
    }

    public void closeReservation(OrderId orderId, BigDecimal totalCost) {
        if (!isReservationForOrder(orderId)) {
            throw new ReservationNotFound();
        }

        Reservation reservation = reservations.get(new ReservationId(id, orderId));

        if (reservation.isClosed()) {
            throw new ReservationClosed();
        }

        reservation.markAsClosed(totalCost);
    }

    public void makeReservation(OrderId orderId, Set<Ticket> tickets) {
        if (isReservationForOrder(orderId)) {
            throw new ReservationAlreadyExist();
        }

        boolean toWaitList = tickets.stream().anyMatch(ticket -> !ticketAvailability.isAvailable(ticket));

        if (toWaitList) {
            waitList.put(new ReservationId(id, orderId), new Reservation(new ReservationId(id, orderId), tickets));
            return;
        }

        tickets.forEach(ticketAvailability::decrementQuantity);

        reservations.put(new ReservationId(id, orderId), new Reservation(new ReservationId(id, orderId), tickets));
    }

    public void cancelReservation(OrderId orderId) {
        if (!isReservationForOrder(orderId)) {
            throw new ReservationNotFound();
        }

        Reservation reservation = reservations.get(new ReservationId(id, orderId));

        if (reservation.isClosed()) {
            throw new ReservationClosed();
        }

        reservation.getTickets().forEach(ticketAvailability::incrementQuantity);

        reservations.remove(reservation.getId());

        for (Reservation waitingReservation: waitList.values()) {
            boolean canBeMoved = true;

            for(Ticket ticket: waitingReservation.getTickets()) {
                if (!ticketAvailability.isAvailable(ticket)) {
                    canBeMoved = false;
                    break;
                }
            }

            if (canBeMoved) {
                waitingReservation.getTickets().forEach(ticketAvailability::decrementQuantity);

                reservations.put(new ReservationId(id, orderId), reservation);
                waitList.remove(reservation.getId());
                break;
            }
        }
    }

    private boolean isReservationForOrder(OrderId orderId) {
        return reservations.containsKey(new ReservationId(id, orderId));
    }

    private class ReservationAlreadyExist extends RuntimeException { }

    private class ReservationNotFound extends RuntimeException { }

    private class ReservationClosed extends RuntimeException { }
}

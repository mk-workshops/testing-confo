package com.mk.workshop.testing.confo.application;

import com.mk.workshop.testing.confo.domain.*;
import com.mk.workshop.testing.confo.infrastructure.ConferenceDao;
import com.mk.workshop.testing.confo.infrastructure.ConferenceRepository;
import com.mk.workshop.testing.confo.infrastructure.PaypalPayments;
import com.mk.workshop.testing.confo.infrastructure.TicketStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Component
public class RegistrationService {
    private static final int MAX_CONFIRMATION_TIME_IN_MINUTES = 15;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceDao conferenceDao;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private PaypalPayments paypalPayments;

    @Autowired
    private TicketStatistics ticketStatistics;

    public String confirmOrder(int orderId, int conferenceId)
    {
        Conference conference = conferenceRepository.get(new ConferenceId(conferenceId));
        Reservation reservation = conference.getReservations().get(new ReservationId(new ConferenceId(conferenceId), new OrderId(orderId)));

        if (reservation.getCreateDate().plusMinutes(MAX_CONFIRMATION_TIME_IN_MINUTES).isAfter(LocalDateTime.now())) {
            throw new OrderExpired();
        }

        BigDecimal totalCost = BigDecimal.ZERO;
        Set<Ticket> tickets = reservation.getTickets();
        Map<String, BigDecimal> ticketPrices = conferenceDao.getTicketPrices(conferenceId);

        for (Ticket ticket : tickets) {
            BigDecimal ticketPrice = ticketPrices.get(ticket.getType());

            BigDecimal discountedPrice = discountService.calculateForTicket(ticket, ticketPrice);
            BigDecimal regularPrice = ticketPrice.multiply(new BigDecimal(ticket.getQuantity()));

            totalCost = totalCost.add(regularPrice.min(discountedPrice));

            ticketStatistics.count(conference.getId(), ticket.getType(), ticket.getQuantity(), LocalDateTime.now());
        }

        conference.closeReservation(new OrderId(orderId), totalCost);

        return paypalPayments.getApprovalLink(new OrderId(orderId), totalCost);
    }

    private class OrderExpired extends RuntimeException { }
}

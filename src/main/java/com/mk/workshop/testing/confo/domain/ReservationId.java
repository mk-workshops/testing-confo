package com.mk.workshop.testing.confo.domain;

import lombok.Value;

@Value
public class ReservationId {
    ConferenceId conferenceId;
    OrderId orderId;
}

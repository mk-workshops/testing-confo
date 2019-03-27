package com.mk.workshop.testing.confo.infrastructure;

import com.mk.workshop.testing.confo.domain.ConferenceId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class TicketStatistics {
    public void count(ConferenceId id, String type, int quantity, LocalDateTime now) {
        log.info(String.format("conference_ticket_count_%s_%s = %s", id.getRaw(), type, quantity));
    }
}

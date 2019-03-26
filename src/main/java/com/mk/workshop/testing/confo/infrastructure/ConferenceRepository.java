package com.mk.workshop.testing.confo.infrastructure;

import com.mk.workshop.testing.confo.domain.Conference;
import com.mk.workshop.testing.confo.domain.ConferenceId;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConferenceRepository {
    private ConcurrentHashMap<ConferenceId, Conference> map = new ConcurrentHashMap<>();

    public Conference get(ConferenceId conferenceId) {
        return map.get(conferenceId);
    }
}

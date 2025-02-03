package ponomarev.dev.eventmanager.events.domain;

import java.time.LocalDateTime;
import java.util.List;


public record Event(
        Long id,

        String name,

        String description,

        Long ownerId,

        Integer maxPlaces,

        Integer occupiedPlaces,

        LocalDateTime date,

        Integer cost,

        Integer duration,

        Long locationId,

        List<EventParticipant> eventParticipantList,

        EventStatus status
) {
}

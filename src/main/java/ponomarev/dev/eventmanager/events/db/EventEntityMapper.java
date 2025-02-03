package ponomarev.dev.eventmanager.events.db;

import org.springframework.stereotype.Component;
import ponomarev.dev.eventmanager.events.domain.Event;
import ponomarev.dev.eventmanager.events.domain.EventParticipant;
import ponomarev.dev.eventmanager.events.domain.EventStatus;


@Component
public class EventEntityMapper {

    public Event toDomain(EventEntity event) {
        return new Event(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getOwnerId(),
                event.getMaxPlaces(),
                event.getEventParticipantList().size(),
                event.getDate(),
                event.getCost(),
                event.getDuration(),
                event.getLocationId(),
                event.getEventParticipantList()
                        .stream().map(eventParticipantEntity -> new EventParticipant(
                                eventParticipantEntity.getId(),
                                eventParticipantEntity.getUserId(),
                                eventParticipantEntity.getEvent().getId()
                        )).toList(),
                event.getStatus()
                );
    }

}

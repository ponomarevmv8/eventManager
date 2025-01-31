package ponomarev.dev.eventmanager.events.api;

import org.springframework.stereotype.Component;
import ponomarev.dev.eventmanager.events.domain.Event;

@Component
public class EventDtoMapper {

    public EventDto toDto(Event event) {
        return new EventDto(
                event.id(),
                event.name(),
                event.description(),
                event.ownerId(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }

}

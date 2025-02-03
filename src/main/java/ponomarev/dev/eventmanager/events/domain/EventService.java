package ponomarev.dev.eventmanager.events.domain;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ponomarev.dev.eventmanager.events.api.EventCreateRequestDto;
import ponomarev.dev.eventmanager.events.api.EventSearchRequestDto;
import ponomarev.dev.eventmanager.events.api.EventUpdateRequestDto;
import ponomarev.dev.eventmanager.events.db.EventEntity;
import ponomarev.dev.eventmanager.events.db.EventEntityMapper;
import ponomarev.dev.eventmanager.events.db.EventRepository;
import ponomarev.dev.eventmanager.location.LocationService;
import ponomarev.dev.eventmanager.security.jwt.JwtAuthenticationService;
import ponomarev.dev.eventmanager.user.domain.User;
import ponomarev.dev.eventmanager.user.domain.UserRole;

import java.util.List;
import java.util.Optional;


@Service
public class EventService {


    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final EventEntityMapper eventEntityMapper;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final LocationService locationService;

    public EventService(EventRepository eventRepository, EventEntityMapper eventEntityMapper, JwtAuthenticationService jwtAuthenticationService, LocationService locationService) {
        this.eventRepository = eventRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.locationService = locationService;
    }

    public Event createEvent(EventCreateRequestDto event) {

        var owner = jwtAuthenticationService.getCurrentAuthenticatedUserOrThrow();
        var location = locationService.findById(event.locationId());

        if (location.capacity() < event.maxPlaces()) {
            throw new IllegalArgumentException("Location capacity < maximum places, %s < %s"
                    .formatted(location.capacity(), event.maxPlaces()));
        }

        var eventToCreat = new EventEntity(
                null,
                event.name(),
                event.description(),
                owner.id(),
                event.maxPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                location.id(),
                List.of(),
                EventStatus.WAIT_START
        );

        var eventCreated = eventRepository.save(eventToCreat);
        log.info("Event created: {}", eventCreated);

        return eventEntityMapper.toDomain(eventCreated);
    }

    public void cancelEvent(Long eventId) {
        var canceledEvent = findById(eventId);
        var user = jwtAuthenticationService.getCurrentAuthenticatedUserOrThrow();

        checkUserCanCancelEvent(user, canceledEvent);

        if (EventStatus.CANCELLED.equals(canceledEvent.status())) {
            log.info("Event id: {} is already cancelled", eventId);
            return;
        }

        if (!EventStatus.WAIT_START.equals(canceledEvent.status())) {
            throw new IllegalArgumentException("Event id %s can't be cancelled because it is not waiting start"
                    .formatted(eventId));
        }
        eventRepository.updateStatus(eventId, EventStatus.CANCELLED.name());
        log.info("Event id: {} was cancelled", eventId);
    }

    public Event findById(Long eventId) {
        log.info("Finding event by id: {}", eventId);
        return eventEntityMapper.toDomain(eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event id: %s not found".formatted(eventId))));
    }

    private void checkUserCanCancelEvent(User user, Event event) {
        if (!user.id().equals(event.ownerId())
            && !UserRole.ADMIN.equals(user.role())) {
            throw new IllegalArgumentException("User id: %s is not created and not admin".formatted(user.id()));
        }
    }

    public Event updateEvent(Long eventId,
                             EventUpdateRequestDto eventDto) {

        var updatedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event id: %s not found".formatted(eventId)));
        var user = jwtAuthenticationService.getCurrentAuthenticatedUserOrThrow();
        checkUserCanCancelEvent(user, eventEntityMapper.toDomain(updatedEvent));

        if (!EventStatus.WAIT_START.name().equals(updatedEvent.getStatus())) {
            throw new IllegalArgumentException("Event id: %s is not waiting start".formatted(eventId));
        }

        if (eventDto.name() != null && eventDto.name().isEmpty()) {
            throw new IllegalArgumentException("Event name: %s cannot be empty".formatted(eventId));
        }


        if (eventDto.maxPlaces() != null || eventDto.locationId() != null) {

            var locationId = eventDto.locationId() != null ? eventDto.locationId() : updatedEvent.getLocationId();

            var maxPlaces = eventDto.maxPlaces() != null ? eventDto.maxPlaces() : updatedEvent.getMaxPlaces();

            if (maxPlaces < updatedEvent.getEventParticipantList().size()) {
                throw new IllegalArgumentException("Event max occupied places: " + eventDto.maxPlaces());
            }

            var location = locationService.findById(locationId);
            if (location.capacity() < maxPlaces) {
                throw new IllegalArgumentException("Location capacity < maximum places, %s < %s"
                        .formatted(location.capacity(), maxPlaces));
            }
        }

        Optional.ofNullable(eventDto.name())
                .ifPresent(updatedEvent::setName);
        Optional.ofNullable(eventDto.description())
                .ifPresent(updatedEvent::setDescription);
        Optional.ofNullable(eventDto.maxPlaces())
                .ifPresent(updatedEvent::setMaxPlaces);
        Optional.ofNullable(eventDto.date())
                .ifPresent(updatedEvent::setDate);
        Optional.ofNullable(eventDto.cost())
                .ifPresent(updatedEvent::setCost);
        Optional.ofNullable(eventDto.duration())
                .ifPresent(updatedEvent::setDuration);
        Optional.ofNullable(eventDto.locationId())
                .ifPresent(updatedEvent::setLocationId);

        log.info("Update event with id: {}", eventId);
        return eventEntityMapper.toDomain(eventRepository.save(updatedEvent));
    }

    public List<Event> searchEvent(EventSearchRequestDto eventSearchRequestDto) {
        log.info("Searching events ");
        return eventRepository.searchEvents(
                        eventSearchRequestDto.name(),
                        eventSearchRequestDto.placesMin(),
                        eventSearchRequestDto.placesMax(),
                        eventSearchRequestDto.dateStartAfter(),
                        eventSearchRequestDto.dateStartBefore(),
                        eventSearchRequestDto.costMin(),
                        eventSearchRequestDto.costMax(),
                        eventSearchRequestDto.durationMin(),
                        eventSearchRequestDto.durationMax(),
                        eventSearchRequestDto.locationId(),
                        eventSearchRequestDto.eventStatus() == null ? null : eventSearchRequestDto.eventStatus().name()
                ).stream().map(eventEntityMapper::toDomain)
                .toList();
    }

    public List<Event> findMyEvents() {
        var user = jwtAuthenticationService.getCurrentAuthenticatedUserOrThrow();
        log.info("Finds all events users id: {}", user.id());
        return eventRepository.findByOwnerId(user.id())
                .stream().map(eventEntityMapper::toDomain)
                .toList();
    }
}

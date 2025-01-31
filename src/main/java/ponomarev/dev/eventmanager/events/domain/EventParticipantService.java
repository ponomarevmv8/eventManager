package ponomarev.dev.eventmanager.events.domain;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ponomarev.dev.eventmanager.events.db.EventEntityMapper;
import ponomarev.dev.eventmanager.events.db.EventParticipantEntity;
import ponomarev.dev.eventmanager.events.db.EventParticipantRepository;
import ponomarev.dev.eventmanager.events.db.EventRepository;
import ponomarev.dev.eventmanager.security.jwt.JwtAuthenticationService;

import java.util.List;


@Service
public class EventParticipantService {


    private static final Logger log = LoggerFactory.getLogger(EventParticipantService.class);
    private final EventParticipantRepository eventParticipantRepository;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final EventRepository eventRepository;
    private final EventEntityMapper eventEntityMapper;

    public EventParticipantService(EventParticipantRepository eventParticipantRepository, JwtAuthenticationService jwtAuthenticationService, EventRepository eventRepository, EventEntityMapper eventEntityMapper) {
        this.eventParticipantRepository = eventParticipantRepository;
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.eventRepository = eventRepository;
        this.eventEntityMapper = eventEntityMapper;
    }

    public void registrationForEvent(Long eventId) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id %s not found".formatted(eventId)));
        if (!EventStatus.WAIT_START.name().equals(event.getStatus())) {
            throw new IllegalArgumentException("Event status is not WAIT_START");
        }

        var user = jwtAuthenticationService.getCurrentAuthenticatedUserOrThrow();
        if (eventParticipantRepository.existsByUserIdAndEventId(user.id(), eventId)) {
            throw new IllegalArgumentException("The user %s was already registered for Event".formatted(user.id()));
        }

        if (event.getEventParticipantList().size() >= event.getMaxPlaces()) {
            throw new IllegalArgumentException("There are no more places to register");
        }

        eventParticipantRepository.save(
                new EventParticipantEntity(
                        null,
                        user.id(),
                        event
                )
        );
        log.info("The user id: {} was registered for the event id: {}", user.id(), eventId);
    }

    public void cancelParticipation(Long eventId) {

        var user = jwtAuthenticationService.getCurrentAuthenticatedUserOrThrow();
        var eventParticipation = eventParticipantRepository.findByUserIdAndEventId(user.id(), eventId)
                .orElseThrow(() -> new EntityNotFoundException("User not registrations for Event id: %s".formatted(eventId)));

        if (!EventStatus.WAIT_START.name().equals(
                eventParticipation.getEvent().getStatus()
        )) {
            throw new IllegalArgumentException("Event status is not WAIT_START");
        }

        eventParticipantRepository.delete(eventParticipation);
        log.info("The user id: {} canceled registration for the event id: {} ", user.id(), eventId);
    }

    public List<Event> findMyParticipation() {
        var user = jwtAuthenticationService.getCurrentAuthenticatedUserOrThrow();
        return eventParticipantRepository.findAllByUserId(user.id())
                .stream()
                .map(EventParticipantEntity::getEvent)
                .map(eventEntityMapper::toDomain)
                .toList();
    }
}

package ponomarev.dev.eventmanager.events.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ponomarev.dev.eventmanager.events.domain.EventParticipantService;

import java.util.List;


@RestController
@RequestMapping("/events/registrations")
public class EventParticipantController {

    private final EventParticipantService eventParticipantService;
    private final EventDtoMapper eventDtoMapper;

    public EventParticipantController(EventParticipantService eventParticipantService, EventDtoMapper eventDtoMapper) {
        this.eventParticipantService = eventParticipantService;
        this.eventDtoMapper = eventDtoMapper;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<String> registrationForEvent(
            @PathVariable("eventId") Long eventId) {
        eventParticipantService.registrationForEvent(eventId);
        return ResponseEntity.ok("Успешная регистрация на мероприятие");
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> cancelParticipation(
            @PathVariable("eventId") Long eventId) {
        eventParticipantService.cancelParticipation(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> findMyParticipation() {
        return ResponseEntity.ok(
                eventParticipantService.findMyParticipation()
                        .stream().map(eventDtoMapper::toDto)
                        .toList()
        );
    }

}

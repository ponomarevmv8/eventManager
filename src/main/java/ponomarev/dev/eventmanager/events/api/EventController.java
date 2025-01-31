package ponomarev.dev.eventmanager.events.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ponomarev.dev.eventmanager.events.domain.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final EventDtoMapper eventDtoMapper;

    public EventController(EventService eventService, EventDtoMapper eventDtoMapper) {
        this.eventService = eventService;
        this.eventDtoMapper = eventDtoMapper;
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody @Valid EventCreateRequestDto eventDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventDtoMapper.toDto(eventService.createEvent(eventDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelEvent(@PathVariable Long id) {
        eventService.cancelEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(eventDtoMapper.toDto(eventService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long id,
            @RequestBody @Valid EventUpdateRequestDto eventDto
    ) {

        return ResponseEntity.ok(eventDtoMapper.toDto(
                eventService.updateEvent(id, eventDto)
        ));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> searchEvent(
            @RequestBody EventSearchRequestDto eventSearchRequestDto
    ) {
        return ResponseEntity.ok(
                eventService.searchEvent(eventSearchRequestDto)
                        .stream()
                        .map(eventDtoMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> findMyEvents() {
        return ResponseEntity.ok(
                eventService.findMyEvents()
                        .stream().map(eventDtoMapper::toDto)
                        .toList()
        );
    }
}

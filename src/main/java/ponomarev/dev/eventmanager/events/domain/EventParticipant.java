package ponomarev.dev.eventmanager.events.domain;

public record EventParticipant(
        Long id,
        Long userId,
        Long event
) {
}

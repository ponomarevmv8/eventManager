package ponomarev.dev.eventmanager.location;

public record Location(
        Long id,
        String name,
        String address,
        String description,
        Integer capacity
) {
}

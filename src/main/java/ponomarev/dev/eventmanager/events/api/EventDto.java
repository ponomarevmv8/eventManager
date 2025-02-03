package ponomarev.dev.eventmanager.events.api;

import jakarta.validation.constraints.*;
import ponomarev.dev.eventmanager.events.domain.EventStatus;
import java.time.LocalDateTime;


public record EventDto(
        @NotNull(message = "Id is mandatory")
        Long id,
        @NotBlank(message = "Name is mandatory")
        String name,

        String description,

        @NotBlank(message = "Owner ID is mandatory")
        Long ownerId,

        @Positive(message = "Maximum places must be greater than zero")
        Integer maxPlaces,

        @PositiveOrZero(message = "Occupied places must be non-negative")
        Integer occupiedPlaces,

        @NotNull(message = "Date is mandatory")
        LocalDateTime date,

        @PositiveOrZero(message = "Cost must be non-negative")
        Integer cost,

        @Min(value = 30, message = "Duration must be at least 30 minutes")
        Integer duration,

        @NotNull(message = "Location is mandatory")
        Long locationId,

        @NotNull(message = "Status is mandatory")
        EventStatus status
) {
}

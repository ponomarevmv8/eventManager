package ponomarev.dev.eventmanager.events.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventUpdateRequestDto(

        String name,

        String description,

        @Positive(message = "Maximum places must be greater than zero")
        Integer maxPlaces,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @Future(message = "Date must be in future")
        LocalDateTime date,

        @PositiveOrZero(message = "Cost must be non-negative")
        Integer cost,

        @Min(value = 30, message = "Duration must be at least 30 minutes")
        Integer duration,

        Long locationId
) {
}

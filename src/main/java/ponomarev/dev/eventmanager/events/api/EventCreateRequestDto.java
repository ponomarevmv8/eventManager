package ponomarev.dev.eventmanager.events.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventCreateRequestDto(
        @NotBlank(message = "Name is mandatory")
        String name,

        String description,

        @Positive(message = "Maximum places must be greater than zero")
        Integer maxPlaces,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @Future(message = "Date must be in future")
        LocalDateTime date,

        @PositiveOrZero(message = "Cost must be positive")
        Integer cost,

        @NotNull
        @Min(value = 30, message = "Duration must be greater than 30")
        Integer duration,

        @NotNull(message = "Location is mandatory")
        Long locationId
) {
}

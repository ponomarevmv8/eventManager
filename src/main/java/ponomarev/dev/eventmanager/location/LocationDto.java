package ponomarev.dev.eventmanager.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LocationDto(
        Long id,

        @NotBlank
        String name,
        @NotBlank
        String address,
        String description,

        @NotNull
        @Min(5)
        Integer capacity
) {
}

package ponomarev.dev.eventmanager.user.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegistration(
        @NotBlank
        String login,

        @NotBlank
        @Size(min = 4, max = 20)
        String password,

        @NotNull
        @Min(18)
        Integer age
) {
}

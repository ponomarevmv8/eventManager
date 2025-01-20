package ponomarev.dev.eventmanager.user.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegistration(
        @NotBlank
        String login,

        @NotBlank
        @Min(4)
        String password,

        @NotNull
        @Min(18)
        Integer age
) {
}

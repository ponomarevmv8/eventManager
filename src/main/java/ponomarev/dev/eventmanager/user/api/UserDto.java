package ponomarev.dev.eventmanager.user.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ponomarev.dev.eventmanager.user.domain.UserRole;

public record UserDto(
        @NotNull
        Long id,

        @NotBlank
        String login,

        @NotNull
        @Min(0)
        Integer age,

        @NotNull
        UserRole role

        ) {
}

package ponomarev.dev.eventmanager.user.api;

import jakarta.validation.constraints.NotBlank;

public record UserCredentials(
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}

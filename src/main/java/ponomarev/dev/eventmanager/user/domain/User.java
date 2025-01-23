package ponomarev.dev.eventmanager.user.domain;

public record User(
        Long id,
        String login,
        Integer age,
        UserRole role
) {
}

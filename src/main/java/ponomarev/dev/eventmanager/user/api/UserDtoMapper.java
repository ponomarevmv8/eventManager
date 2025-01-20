package ponomarev.dev.eventmanager.user.api;

import org.springframework.stereotype.Component;
import ponomarev.dev.eventmanager.user.domain.User;

@Component
public class UserDtoMapper {

    UserDto toDto(User user) {
        return new UserDto(
                user.id(),
                user.login(),
                user.age(),
                user.role()
        );
    }

}

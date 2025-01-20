package ponomarev.dev.eventmanager.user.domain;

import org.springframework.stereotype.Component;
import ponomarev.dev.eventmanager.user.db.UserEntity;

@Component
public class UserMapper {

    public User toDomain (UserEntity user){
        return new User(
                user.getId(),
                user.getLogin(),
                user.getAge(),
                UserRole.valueOf(user.getRole())
        );
    }
}

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

    public UserEntity toEntity (User user){
        return new UserEntity(
                user.id(),
                user.login(),
                null,
                user.age(),
                user.role().name()
        );
    }
}

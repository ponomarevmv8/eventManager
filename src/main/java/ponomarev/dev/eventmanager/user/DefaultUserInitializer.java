package ponomarev.dev.eventmanager.user;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ponomarev.dev.eventmanager.user.db.UserEntity;
import ponomarev.dev.eventmanager.user.db.UserRepository;
import ponomarev.dev.eventmanager.user.domain.UserRole;

@Component
public class DefaultUserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initUsers(){
        createUser("admin", "admin", UserRole.ADMIN);
        createUser("user", "user", UserRole.USER);
    }

    private void createUser(String login, String password, UserRole role) {
        if(userRepository.existsByLogin(login)) {
            return;
        }

        var user = new UserEntity(
                null,
                login,
                passwordEncoder.encode(password),
                20,
                role.name()
        );

        userRepository.save(user);
    }


}

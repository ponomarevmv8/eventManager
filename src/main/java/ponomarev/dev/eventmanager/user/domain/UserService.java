package ponomarev.dev.eventmanager.user.domain;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ponomarev.dev.eventmanager.user.api.UserRegistration;
import ponomarev.dev.eventmanager.user.db.UserEntity;
import ponomarev.dev.eventmanager.user.db.UserRepository;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserRegistration userRegistration) {
        if (userRepository.existsByLogin(userRegistration.login()))
            throw new IllegalArgumentException("Login already exists");
        var hashedPassword = passwordEncoder.encode(userRegistration.password());
        var createUser = new UserEntity(
                null,
                userRegistration.login(),
                hashedPassword,
                userRegistration.age(),
                UserRole.USER.name()
        );

        return userMapper.toDomain(
                userRepository.save(createUser)
        );
    }

    public User findById(Long id) {
        return userMapper.toDomain(
                userRepository.findById(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException("User not found with id %s".formatted(id))
                        )
        );
    }

    public User findByLogin(String login) {
        return userMapper.toDomain(
                userRepository.findByLogin(login)
                        .orElseThrow(
                                () -> new EntityNotFoundException("User not found with login %s".formatted(login))
                        )
        );
    }
}

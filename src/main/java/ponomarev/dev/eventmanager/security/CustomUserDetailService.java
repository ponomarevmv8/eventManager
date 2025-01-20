package ponomarev.dev.eventmanager.security;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ponomarev.dev.eventmanager.user.db.UserRepository;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var findUser = userRepository.findByLogin(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " not found"));
        return User.withUsername(findUser.getLogin())
                .password(findUser.getPassword())
                .authorities(findUser.getRole())
                .build();
    }
}

package ponomarev.dev.eventmanager.security.jwt;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ponomarev.dev.eventmanager.user.api.UserCredentials;
import ponomarev.dev.eventmanager.user.domain.User;
import ponomarev.dev.eventmanager.user.domain.UserService;

@Service
public class JwtAuthenticationService {

    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public JwtAuthenticationService(JwtTokenManager jwtTokenManager,
                                    AuthenticationManager authenticationManager, UserService userService) {
        this.jwtTokenManager = jwtTokenManager;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public String authenticate(UserCredentials userCredentials) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userCredentials.login(),
                userCredentials.password()
        ));
        var user = userService.findByLogin(userCredentials.login());
        return jwtTokenManager.generateToken(user);
    }

    public User getCurrentAuthenticatedUserOrThrow() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("No authentication found");
        }
        return (User) authentication.getPrincipal();
    }
}

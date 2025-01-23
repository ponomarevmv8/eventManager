package ponomarev.dev.eventmanager.security.jwt;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ponomarev.dev.eventmanager.user.api.UserCredentials;
import ponomarev.dev.eventmanager.user.domain.User;

@Service
public class JwtAuthenticationService {

    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationService(JwtTokenManager jwtTokenManager,
                                    AuthenticationManager authenticationManager) {
        this.jwtTokenManager = jwtTokenManager;
        this.authenticationManager = authenticationManager;
    }

    public String authenticate(UserCredentials userCredentials) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userCredentials.login(),
                userCredentials.password()
        ));

        return jwtTokenManager.generateToken(userCredentials.login());
    }

    public User getCurrentAuthenticatedUserOrThrow() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("No authentication found");
        }
        return (User) authentication.getPrincipal();
    }
}

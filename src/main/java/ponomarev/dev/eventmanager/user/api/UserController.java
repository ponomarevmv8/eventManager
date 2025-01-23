package ponomarev.dev.eventmanager.user.api;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ponomarev.dev.eventmanager.security.jwt.JwtAuthenticationService;
import ponomarev.dev.eventmanager.security.jwt.JwtTokenResponce;
import ponomarev.dev.eventmanager.user.domain.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserDtoMapper userDtoMapper;
    private final UserService userService;
    private final JwtAuthenticationService jwtAuthenticationService;

    public UserController(UserDtoMapper userDtoMapper, UserService userService, JwtAuthenticationService jwtAuthenticationService) {
        this.userDtoMapper = userDtoMapper;
        this.userService = userService;
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody @Valid UserRegistration userRegistration
    ) {
        log.info("Creating user: {}", userRegistration.login());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                    userDtoMapper.toDto(
                            userService.createUser(userRegistration)
                    )
                );
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponce> authenticateUser(
            @RequestBody @Valid UserCredentials userCredentials
    ) {
        return ResponseEntity.ok(
                new JwtTokenResponce(
                        jwtAuthenticationService.authenticate(userCredentials)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(
            @PathVariable Long id
    ) {
        log.info("Finding user by id: {}", id);
        return ResponseEntity.ok(userDtoMapper.toDto(
                userService.findById(id)
        ));
    }

}

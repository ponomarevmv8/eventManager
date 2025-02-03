package ponomarev.dev.eventmanager.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ponomarev.dev.eventmanager.user.domain.UserService;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {


    private final JwtTokenManager jwtTokenManager;

    private final UserService userService;

    public JwtTokenFilter(JwtTokenManager jwtTokenManager,
                          @Lazy UserService userService) {
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = authHeader.replace("Bearer ", "");
        String login;
        try {
            login = jwtTokenManager.getLoginFromToken(token);
        } catch (Exception e) {
            logger.error("Error while getting login from token", e);
            filterChain.doFilter(request, response);
            return;
        }

        var user = userService.findByLogin(login);

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority(user.role().name())));
        SecurityContextHolder.getContext().setAuthentication(userToken);
        filterChain.doFilter(request, response);
    }
}

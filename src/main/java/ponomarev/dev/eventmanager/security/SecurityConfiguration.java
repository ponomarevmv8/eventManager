package ponomarev.dev.eventmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ponomarev.dev.eventmanager.security.jwt.JwtTokenFilter;
import ponomarev.dev.eventmanager.user.domain.UserRole;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/webjars/**",
                                        "/openapi.yaml",      // YAML-спецификация
                                        "/v3/api-docs.yaml",  // Альтернативный путь
                                        "/v3/api-docs"
                                ).permitAll()

                                .requestMatchers(HttpMethod.POST, "users")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/auth")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/{id}")
                                .hasAnyAuthority(UserRole.ADMIN.name())

                                .requestMatchers(HttpMethod.GET, "locations/**")
                                .hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                                .requestMatchers(HttpMethod.POST, "locations")
                                .hasAnyAuthority(UserRole.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "locations/{id}")
                                .hasAnyAuthority(UserRole.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "locations/{id}")
                                .hasAnyAuthority(UserRole.ADMIN.name())

                                .anyRequest()
                                .authenticated())
                .exceptionHandling(exceptions ->
                        exceptions
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(jwtTokenFilter, AnonymousAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

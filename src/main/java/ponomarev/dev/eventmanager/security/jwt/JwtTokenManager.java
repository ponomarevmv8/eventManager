package ponomarev.dev.eventmanager.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ponomarev.dev.eventmanager.user.domain.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenManager {

    private final SecretKey secretKey;
    private final long lifeTime;

    public JwtTokenManager(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.life-time}") long lifeTime) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.lifeTime = lifeTime;
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.login())
                .claim("userId", user.id())
                .claim("role", user.role().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifeTime))
                .signWith(secretKey)
                .compact();
    }

    public String getLoginFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}

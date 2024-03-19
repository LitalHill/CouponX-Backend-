package com.authforge.service;

import com.authforge.data.entity.User;
import com.authforge.data.repository.UserRepository;
import com.authforge.web.dto.UserDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthForgeService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration.millis}")
    private long jwtExpirationMillis;

    @Override
    public String authenticateAndGenerateToken(String username,
                                               String password) throws AuthException {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty() || !passwordEncoder.matches(password,
                                                          optUser.get().getBcryptPassword())) {
            throw new AuthException("Authentication failed");
        }

        User user = optUser.get();

        return generateJwtToken(user.getUsername(), user.getUuid());
    }

    @Override
    public UserDto parseTokenAndGetUser(String token) {
        // Fetch the JWT (JSON Web Token) secret from environment variables
        String jwtSecret = System.getenv("JWT_SECRET");

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token);

        Claims claims = claimsJws.getBody();


        String username = claims.get("username", String.class);
        String uuidStr = claims.get("uuid", String.class);

        return new UserDto(username, UUID.fromString(uuidStr));
    }

    @Override
    public boolean signUp(String username, String password) {

        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isPresent()) {
            return false;
        }

        User newUser = User.builder()
                .username(username)
                .bcryptPassword(passwordEncoder.encode(password))
                .build();

        userRepository.save(newUser);
        return true;
    }

    private String generateJwtToken(String username, UUID uuid) {
        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationMillis);
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("uuid", uuid);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(System.getenv("JWT_SECRET").getBytes()))
                .compact();
    }

}

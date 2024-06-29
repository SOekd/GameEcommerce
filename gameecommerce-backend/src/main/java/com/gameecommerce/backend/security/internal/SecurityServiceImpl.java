package com.gameecommerce.backend.security.internal;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gameecommerce.backend.security.AuthenticationRequest;
import com.gameecommerce.backend.security.AuthenticationResponse;
import com.gameecommerce.backend.security.SecurityService;
import com.gameecommerce.backend.security.exception.AuthenticationException;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        val user = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        if (user == null) {
            throw new AuthenticationException("User not found");
        }

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        val token = createToken(user);
        return new AuthenticationResponse(token);
    }

    @Override
    public @Nullable UserDetails extractUser(String token) {
        if (!verifyToken(token)) {
            return null;
        }

        val subject = getVerifier().verify(token).getSubject();

        return userDetailsService.loadUserByUsername(subject);
    }


    @Override
    public boolean verifyToken(String token) {
        try {
            getVerifier().verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    private String createToken(UserDetails user) {
        return JWT.create()
                .withIssuer("game-ecommerce")
                .withSubject(user.getUsername())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(60 * 60 * 24))
                .sign(getAlgorithm());
    }

    private JWTVerifier getVerifier() {
        return JWT.require(getAlgorithm())
                .withIssuer("game-ecommerce")
                .build();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(getJwtKey());
    }

    private String getJwtKey() {
        return System.getenv("JWT_KEY");
    }

}

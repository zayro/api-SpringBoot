package com.rest.api.infrastructure.adapter.input.web;

import com.rest.api.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final JwtService jwtService;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody AuthRequest req) {
        // For demo purposes, we accept any non-empty username/password
        // In production, validate against a user database
        if (req.username().isEmpty() || req.password().isEmpty()) {
            log.warn("Login attempt with empty credentials");
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        // Simple validation: username must match password for demo (e.g., admin/admin)
        if (!validateCredentials(req.username(), req.password())) {
            log.warn("Login failed for user: {}", req.username());
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        String token = jwtService.generateToken(req.username());
        log.info("Login successful for user: {}", req.username());
        return Mono.just(ResponseEntity.ok(new AuthResponse(token, req.username(), jwtExpiration / 1000)));
    }

    private boolean validateCredentials(String username, String password) {
        // Demo: accept admin/admin, user/user
        return (username.equals("admin") && password.equals("admin"))
                || (username.equals("user") && password.equals("user"));
    }
}

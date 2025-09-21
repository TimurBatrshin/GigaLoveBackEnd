package com.gigalove.auth;

import com.gigalove.auth.dto.AuthDtos;
import com.gigalove.user.User;
import com.gigalove.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final com.gigalove.security.JwtService jwtService;
    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

    public AuthController(UserRepository userRepository, com.gigalove.security.JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDtos.AuthResponse> register(@RequestBody @Valid AuthDtos.RegisterRequest req) {
        User u = userRepository.findByEmail(req.getEmail()).orElseGet(User::new);
        if (u.getId() != null) {
            return ResponseEntity.status(409).build();
        }
        u.setEmail(req.getEmail());
        u.setPhone(req.getPhone());
        u.setName(req.getName());
        u.setPasswordHash(hash(req.getPassword()));
        u.setCreatedAt(Instant.now());
        userRepository.save(u);
        String token = jwtService.generate(u.getId());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, String.valueOf(u.getId())));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthResponse> login(@RequestBody @Valid AuthDtos.LoginRequest req) {
        var userOpt = userRepository.findByEmail(req.getEmail());
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();
        var user = userOpt.get();
        if (!hash(req.getPassword()).equals(user.getPasswordHash())) return ResponseEntity.status(401).build();
        String token = jwtService.generate(user.getId());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, String.valueOf(user.getId())));
    }

    private String hash(String s) { return Integer.toHexString(s.hashCode()); }
}



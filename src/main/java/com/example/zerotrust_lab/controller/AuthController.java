package com.example.zerotrust_lab.controller;

import com.example.zerotrust_lab.model.User;
import com.example.zerotrust_lab.repository.UserRepository;
import com.example.zerotrust_lab.service.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =====================================================
    // REGISTER
    // =====================================================

    @PostMapping("/register")
    public Map<String, String> register(
            @RequestBody User user) {

        if (user.getUsername() == null
                || user.getUsername().trim().isEmpty()) {

            return Map.of(
                    "message",
                    "Username is required"
            );
        }

        if (user.getPassword() == null
                || user.getPassword().trim().isEmpty()) {

            return Map.of(
                    "message",
                    "Password is required"
            );
        }

        if (user.getRole() == null
                || user.getRole().trim().isEmpty()) {

            return Map.of(
                    "message",
                    "Role is required"
            );
        }

        String username =
                user.getUsername().trim();

        String role =
                user.getRole().trim().toUpperCase();

        // =================================================
        // CHECK USERNAME
        // =================================================

        if (userRepository
                .findByUsername(username)
                .isPresent()) {

            return Map.of(
                    "message",
                    "Username already exists"
            );
        }

        // =================================================
        // CHECK ROLE
        // =================================================

        if (!role.equals("RESEARCHER")
                && !role.equals("LAB_STAFF")
                && !role.equals("INTERN")
                && !role.equals("LAB_ADMIN")) {

            return Map.of(
                    "message",
                    "Invalid role"
            );
        }

        // =================================================
        // CREATE USER
        // =================================================

        user.setUsername(username);
        user.setRole(role);

        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()
                )
        );

        // MFA disabled
        user.setMfaEnabled(false);

        // Device trusted
        user.setDeviceTrusted(true);

        // Account active
        user.setActive(true);

        userRepository.save(user);

        return Map.of(
                "message",
                "User registered successfully",
                "username",
                username,
                "role",
                role
        );
    }

    // =====================================================
    // LOGIN
    // =====================================================

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody Map<String, String> request) {

        String username =
                request.get("username");

        String password =
                request.get("password");

        // =================================================
        // VALIDATION
        // =================================================

        if (username == null
                || username.trim().isEmpty()) {

            return Map.of(
                    "message",
                    "Username is required"
            );
        }

        if (password == null
                || password.trim().isEmpty()) {

            return Map.of(
                    "message",
                    "Password is required"
            );
        }

        username = username.trim();

        // =================================================
        // FIND USER
        // =================================================

        User user =
                userRepository
                        .findByUsername(username)
                        .orElse(null);

        if (user == null) {

            return Map.of(
                    "message",
                    "Invalid username or password"
            );
        }

        // =================================================
        // CHECK ACCOUNT
        // =================================================

        if (!user.isActive()) {

            return Map.of(
                    "message",
                    "User account is inactive"
            );
        }

        // =================================================
        // CHECK PASSWORD
        // =================================================

        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            return Map.of(
                    "message",
                    "Invalid username or password"
            );
        }

        // =================================================
        // DIRECT JWT
        // =================================================

        String token =
                jwtService.generateToken(
                        username,
                        user.getRole()
                );

        // =================================================
        // LOGIN SUCCESS
        // =================================================

        return Map.of(
                "message",
                "Login successful",
                "token",
                token,
                "username",
                username,
                "role",
                user.getRole()
        );
    }
}
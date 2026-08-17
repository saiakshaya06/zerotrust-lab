package com.example.zerotrust_lab.service;

import com.example.zerotrust_lab.model.User;
import com.example.zerotrust_lab.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
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

    public String register(
            String username,
            String password,
            String role) {

        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        if (role == null || role.trim().isEmpty()) {
            throw new RuntimeException("Role is required");
        }

        username = username.trim();
        role = role.trim().toUpperCase();

        // Check duplicate username
        if (userRepository
                .findByUsername(username)
                .isPresent()) {

            throw new RuntimeException(
                    "Username already exists");
        }

        // Check valid role
        if (!role.equals("RESEARCHER")
                && !role.equals("LAB_STAFF")
                && !role.equals("INTERN")
                && !role.equals("LAB_ADMIN")) {

            throw new RuntimeException(
                    "Invalid role");
        }

        // Create user
        User user = new User();

        user.setUsername(username);

        user.setPassword(
                passwordEncoder.encode(password)
        );

        user.setRole(role);

        // MFA removed
        user.setMfaEnabled(false);

        // Demo device is trusted
        user.setDeviceTrusted(true);

        // Account active
        user.setActive(true);

        userRepository.save(user);

        return "User registered successfully";
    }

    // =====================================================
    // LOGIN
    // =====================================================

    public String login(
            String username,
            String password) {

        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException(
                    "Username is required");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException(
                    "Password is required");
        }

        username = username.trim();

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username or password"));

        // Check account
        if (!user.isActive()) {

            throw new RuntimeException(
                    "User account is inactive");
        }

        // Check password
        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid username or password");
        }

        // =================================================
        // DIRECT JWT LOGIN
        // NO OTP
        // =================================================

        return jwtService.generateToken(
                user.getUsername(),
                user.getRole()
        );
    }
}
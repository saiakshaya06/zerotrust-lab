package com.example.zerotrust_lab.service;

import com.example.zerotrust_lab.model.User;
import com.example.zerotrust_lab.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final Map<String, String> otpStore =
            new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(String username,
                           String password,
                           String role) {

        if (userRepository.findByUsername(username).isPresent()) {

            throw new RuntimeException(
                    "Username already exists");
        }

        role = role.toUpperCase();

        if (!role.equals("RESEARCHER")
                && !role.equals("INTERN")
                && !role.equals("LAB_STAFF")
                && !role.equals("LAB_ADMIN")) {

            throw new RuntimeException(
                    "Invalid role");
        }

        User user = new User(
                username,
                passwordEncoder.encode(password),
                role,
                true,
                true,
                true
        );

        userRepository.save(user);

        return "User registered successfully";
    }

    public String login(String username,
                        String password) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        if (!user.isActive()) {

            throw new RuntimeException(
                    "User account is inactive");
        }

        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid password");
        }

        if (user.isMfaEnabled()) {

            String otp = String.format(
                    "%06d",
                    new Random().nextInt(1000000));

            otpStore.put(username, otp);

            System.out.println(
                    "================================");

            System.out.println(
                    "LAB MFA OTP for "
                    + username
                    + " = "
                    + otp);

            System.out.println(
                    "================================");

            return "MFA_REQUIRED";
        }

        return jwtService.generateToken(
                user.getUsername(),
                user.getRole());
    }

    public String verifyOtp(String username,
                            String otp) {

        String storedOtp =
                otpStore.get(username);

        if (storedOtp == null
                || !storedOtp.equals(otp)) {

            throw new RuntimeException(
                    "Invalid OTP");
        }

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        otpStore.remove(username);

        return jwtService.generateToken(
                user.getUsername(),
                user.getRole());
    }
}
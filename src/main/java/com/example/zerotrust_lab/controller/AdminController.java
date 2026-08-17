package com.example.zerotrust_lab.controller;

import com.example.zerotrust_lab.model.AccessLog;
import com.example.zerotrust_lab.repository.AccessLogRepository;
import com.example.zerotrust_lab.service.JwtService;
import com.example.zerotrust_lab.repository.UserRepository;
import com.example.zerotrust_lab.model.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lab/admin")
public class AdminController {

    private final AccessLogRepository accessLogRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AdminController(
            AccessLogRepository accessLogRepository,
            JwtService jwtService,
            UserRepository userRepository) {

        this.accessLogRepository = accessLogRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @GetMapping("/access-logs")
    public ResponseEntity<?> getAccessLogs(

            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorization) {

        // ==========================================
        // STEP 1: JWT CHECK
        // ==========================================

        if (authorization == null
                || !authorization.startsWith("Bearer ")) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "message",
                                    "JWT token required"
                            )
                    );
        }

        // ==========================================
        // STEP 2: EXTRACT TOKEN
        // ==========================================

        String token =
                authorization.substring(7);

        // ==========================================
        // STEP 3: VALIDATE TOKEN
        // ==========================================

        if (!jwtService.isValid(token)) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "message",
                                    "Invalid or expired JWT"
                            )
                    );
        }

        // ==========================================
        // STEP 4: IDENTIFY USER
        // ==========================================

        String username =
                jwtService.extractUsername(token);

        User user =
                userRepository
                        .findByUsername(username)
                        .orElse(null);

        if (user == null) {

            return ResponseEntity
                    .status(403)
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "message",
                                    "User not found"
                            )
                    );
        }

        // ==========================================
        // STEP 5: CHECK LAB ADMIN ROLE
        // ==========================================

        if (!user.getRole()
                .equalsIgnoreCase("LAB_ADMIN")) {

            return ResponseEntity
                    .status(403)
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "user",
                                    username,

                                    "message",
                                    "Only LAB_ADMIN can access audit logs"
                            )
                    );
        }

        // ==========================================
        // STEP 6: ALLOW ACCESS
        // ==========================================

        List<AccessLog> logs =
                accessLogRepository
                        .findAllByOrderByTimestampDesc();

        return ResponseEntity.ok(
                Map.of(
                        "decision",
                        "ALLOWED",

                        "user",
                        username,

                        "message",
                        "Lab Admin access granted",

                        "accessLogs",
                        logs
                )
        );
    }
}
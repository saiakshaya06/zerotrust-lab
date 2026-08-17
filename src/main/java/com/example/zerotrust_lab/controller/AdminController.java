package com.example.zerotrust_lab.controller;

import com.example.zerotrust_lab.model.AccessLog;
import com.example.zerotrust_lab.repository.AccessLogRepository;
import com.example.zerotrust_lab.repository.UserRepository;
import com.example.zerotrust_lab.model.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lab/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AccessLogRepository accessLogRepository;
    private final UserRepository userRepository;

    public AdminController(
            AccessLogRepository accessLogRepository,
            UserRepository userRepository) {

        this.accessLogRepository = accessLogRepository;
        this.userRepository = userRepository;
    }


    // ==========================================
    // ACCESS LOGS
    // NO JWT
    // ==========================================

    @GetMapping("/access-logs")
    public ResponseEntity<?> getAccessLogs(

            @RequestHeader(
                    value = "X-Username",
                    required = false
            )
            String username) {

        // ==========================================
        // 1. USERNAME CHECK
        // ==========================================

        if (username == null ||
                username.trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "message",
                                    "Username required"
                            )
                    );
        }

        username = username.trim();


        // ==========================================
        // 2. FIND USER
        // ==========================================

        User user = userRepository
                .findByUsername(username)
                .orElse(null);

        if (user == null) {

            return ResponseEntity
                    .status(404)
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "user",
                                    username,

                                    "message",
                                    "User not found"
                            )
                    );
        }


        // ==========================================
        // 3. ACCOUNT CHECK
        // ==========================================

        if (!user.isActive()) {

            return ResponseEntity
                    .status(403)
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "user",
                                    username,

                                    "message",
                                    "User account is inactive"
                            )
                    );
        }


        // ==========================================
        // 4. ROLE CHECK
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
        // 5. DEVICE TRUST
        // ==========================================

        if (!user.isDeviceTrusted()) {

            return ResponseEntity
                    .status(403)
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "user",
                                    username,

                                    "message",
                                    "Device is not trusted"
                            )
                    );
        }


        // ==========================================
        // 6. ALLOW
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
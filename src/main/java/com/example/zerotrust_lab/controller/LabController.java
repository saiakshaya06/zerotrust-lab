package com.example.zerotrust_lab.controller;

import com.example.zerotrust_lab.service.JwtService;
import com.example.zerotrust_lab.service.ZeroTrustPolicyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/lab")
public class LabController {

    private final ZeroTrustPolicyService policyService;

    private final JwtService jwtService;

    public LabController(
            ZeroTrustPolicyService policyService,
            JwtService jwtService) {

        this.policyService = policyService;
        this.jwtService = jwtService;
    }

    // ==========================================
    // RESEARCH
    // ==========================================

    @GetMapping("/research")
    public ResponseEntity<?> research(
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorization) {

        return check(
                authorization,
                "/lab/research",
                "Research Laboratory"
        );
    }

    // ==========================================
    // EXPERIMENTS
    // ==========================================

    @GetMapping("/experiments")
    public ResponseEntity<?> experiments(
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorization) {

        return check(
                authorization,
                "/lab/experiments",
                "Experiment Resources"
        );
    }

    // ==========================================
    // INTERN
    // ==========================================

    @GetMapping("/intern")
    public ResponseEntity<?> intern(
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorization) {

        return check(
                authorization,
                "/lab/intern",
                "Intern Laboratory Resources"
        );
    }

    // ==========================================
    // EQUIPMENT
    // ==========================================

    @GetMapping("/equipment")
    public ResponseEntity<?> equipment(
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorization) {

        return check(
                authorization,
                "/lab/equipment",
                "Laboratory Equipment"
        );
    }

    // ==========================================
    // OPERATIONS
    // ==========================================

    @GetMapping("/operations")
    public ResponseEntity<?> operations(
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorization) {

        return check(
                authorization,
                "/lab/operations",
                "Laboratory Operations"
        );
    }

    // ==========================================
    // ADMIN
    // ==========================================

    @GetMapping("/admin")
    public ResponseEntity<?> admin(
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorization) {

        return check(
                authorization,
                "/lab/admin",
                "Laboratory Administration"
        );
    }

    // ==========================================
    // ZERO TRUST CHECK
    // ==========================================

    private ResponseEntity<?> check(
            String authorization,
            String resource,
            String resourceName) {

        // STEP 1: Check JWT exists

        if (authorization == null
                || !authorization.startsWith("Bearer ")) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "resource",
                                    resourceName,

                                    "message",
                                    "JWT token required"
                            )
                    );
        }

        // STEP 2: Extract JWT

        String token =
                authorization.substring(7);

        // STEP 3: Validate JWT

        if (!jwtService.isValid(token)) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "resource",
                                    resourceName,

                                    "message",
                                    "Invalid or expired JWT"
                            )
                    );
        }

        // STEP 4: Identify user

        String username =
                jwtService.extractUsername(token);

        // STEP 5: Apply Zero Trust policy

        String result =
                policyService.checkAccess(
                        username,
                        resource
                );

        // STEP 6: Access decision

        if (result.startsWith(
                "ACCESS ALLOWED")) {

            return ResponseEntity.ok(
                    Map.of(
                            "decision",
                            "ALLOWED",

                            "user",
                            username,

                            "resource",
                            resourceName,

                            "message",
                            result
                    )
            );
        }

        // STEP 7: Deny access

        return ResponseEntity
                .status(403)
                .body(
                        Map.of(
                                "decision",
                                "DENIED",

                                "user",
                                username,

                                "resource",
                                resourceName,

                                "message",
                                result
                        )
                );
    }
}
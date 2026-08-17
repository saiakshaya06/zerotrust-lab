package com.example.zerotrust_lab.controller;

import com.example.zerotrust_lab.service.ZeroTrustPolicyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/lab")
@CrossOrigin(origins = "*")
public class LabController {

    private final ZeroTrustPolicyService policyService;

    public LabController(ZeroTrustPolicyService policyService) {
        this.policyService = policyService;
    }

    // ==========================================
    // RESEARCH
    // ==========================================

    @GetMapping("/research")
    public ResponseEntity<?> research(
            @RequestHeader(
                    value = "X-Username",
                    required = false
            )
            String username) {

        return check(
                username,
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
                    value = "X-Username",
                    required = false
            )
            String username) {

        return check(
                username,
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
                    value = "X-Username",
                    required = false
            )
            String username) {

        return check(
                username,
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
                    value = "X-Username",
                    required = false
            )
            String username) {

        return check(
                username,
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
                    value = "X-Username",
                    required = false
            )
            String username) {

        return check(
                username,
                "/lab/operations",
                "Laboratory Operations"
        );
    }

    // ==========================================
    // ADMINISTRATION
    // ==========================================

    @GetMapping("/admin")
    public ResponseEntity<?> admin(
            @RequestHeader(
                    value = "X-Username",
                    required = false
            )
            String username) {

        return check(
                username,
                "/lab/admin",
                "Laboratory Administration"
        );
    }

    // ==========================================
    // ZERO TRUST POLICY CHECK
    // ==========================================

    private ResponseEntity<?> check(
            String username,
            String resource,
            String resourceName) {

        // ------------------------------------------
        // 1. USERNAME REQUIRED
        // ------------------------------------------

        if (username == null || username.trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "resource",
                                    resourceName,

                                    "message",
                                    "Username required"
                            )
                    );
        }

        username = username.trim();

        // ------------------------------------------
        // 2. ZERO TRUST POLICY
        // ------------------------------------------

        String result;

        try {

            result = policyService.checkAccess(
                    username,
                    resource
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(404)
                    .body(
                            Map.of(
                                    "decision",
                                    "DENIED",

                                    "user",
                                    username,

                                    "resource",
                                    resourceName,

                                    "message",
                                    e.getMessage()
                            )
                    );
        }

        // ------------------------------------------
        // 3. ALLOWED
        // ------------------------------------------

        if (result.startsWith("ACCESS ALLOWED")) {

            return ResponseEntity
                    .ok()
                    .body(
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

        // ------------------------------------------
        // 4. DENIED
        // ------------------------------------------

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
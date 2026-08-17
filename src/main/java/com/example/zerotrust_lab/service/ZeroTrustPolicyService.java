package com.example.zerotrust_lab.service;

import com.example.zerotrust_lab.model.AccessLog;
import com.example.zerotrust_lab.model.User;
import com.example.zerotrust_lab.repository.AccessLogRepository;
import com.example.zerotrust_lab.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class ZeroTrustPolicyService {

    private final UserRepository userRepository;
    private final AccessLogRepository accessLogRepository;

    public ZeroTrustPolicyService(
            UserRepository userRepository,
            AccessLogRepository accessLogRepository) {

        this.userRepository = userRepository;
        this.accessLogRepository = accessLogRepository;
    }

    public String checkAccess(
            String username,
            String resource) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        String role = user.getRole().toUpperCase();

        // ==========================================
        // 1. ACCOUNT STATUS
        // ==========================================

        if (!user.isActive()) {

            saveLog(
                    user,
                    resource,
                    "DENIED",
                    "User account is inactive"
            );

            return "ACCESS DENIED: User account is inactive";
        }

        // ==========================================
        // 2. DEVICE TRUST
        // ==========================================

        if (!user.isDeviceTrusted()) {

            saveLog(
                    user,
                    resource,
                    "DENIED",
                    "Device is not trusted"
            );

            return "ACCESS DENIED: Device is not trusted";
        }

        boolean allowed = false;

        // ==========================================
        // 3. ROLE BASED ZERO TRUST POLICY
        // ==========================================

        switch (role) {

            case "RESEARCHER":

                if (resource.equals("/lab/research")
                        || resource.equals("/lab/experiments")
                        || resource.equals("/lab/equipment")
                        || resource.equals("/lab/operations")) {

                    allowed = true;
                }

                break;

            case "LAB_STAFF":

                if (resource.equals("/lab/research")
                        || resource.equals("/lab/experiments")
                        || resource.equals("/lab/equipment")
                        || resource.equals("/lab/operations")) {

                    allowed = true;
                }

                break;

            case "INTERN":

                if (resource.equals("/lab/intern")) {

                    allowed = true;
                }

                break;

            case "LAB_ADMIN":

                allowed = true;

                break;

            default:

                saveLog(
                        user,
                        resource,
                        "DENIED",
                        "Unknown user role"
                );

                return "ACCESS DENIED: Unknown user role";
        }

        // ==========================================
        // 4. FINAL DECISION
        // ==========================================

        if (allowed) {

            String message =
                    "ACCESS ALLOWED: "
                    + role
                    + " has permission for "
                    + resource;

            saveLog(
                    user,
                    resource,
                    "ALLOWED",
                    message
            );

            return message;
        }

        String message =
                "ACCESS DENIED: "
                + role
                + " does not have permission for "
                + resource;

        saveLog(
                user,
                resource,
                "DENIED",
                message
        );

        return message;
    }

    // ==========================================
    // ACCESS LOG
    // ==========================================

    private void saveLog(
            User user,
            String resource,
            String decision,
            String reason) {

        AccessLog log = new AccessLog(
                user.getUsername(),
                user.getRole(),
                resource,
                decision,
                reason,
                user.isDeviceTrusted()
        );

        accessLogRepository.save(log);
    }
}
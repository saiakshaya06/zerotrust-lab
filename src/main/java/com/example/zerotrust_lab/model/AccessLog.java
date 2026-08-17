package com.example.zerotrust_lab.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_logs")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String role;

    private String resource;

    private String decision;

    private String reason;

    private boolean deviceTrusted;

    private LocalDateTime timestamp;

    public AccessLog() {
    }

    public AccessLog(String username,
                     String role,
                     String resource,
                     String decision,
                     String reason,
                     boolean deviceTrusted) {

        this.username = username;
        this.role = role;
        this.resource = resource;
        this.decision = decision;
        this.reason = reason;
        this.deviceTrusted = deviceTrusted;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getResource() {
        return resource;
    }

    public String getDecision() {
        return decision;
    }

    public String getReason() {
        return reason;
    }

    public boolean isDeviceTrusted() {
        return deviceTrusted;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
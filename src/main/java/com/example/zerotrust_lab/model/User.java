package com.example.zerotrust_lab.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

    private boolean mfaEnabled;

    private boolean deviceTrusted;

    private boolean active;

    public User() {
    }

    public User(String username,
                String password,
                String role,
                boolean mfaEnabled,
                boolean deviceTrusted,
                boolean active) {

        this.username = username;
        this.password = password;
        this.role = role;
        this.mfaEnabled = mfaEnabled;
        this.deviceTrusted = deviceTrusted;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public boolean isDeviceTrusted() {
        return deviceTrusted;
    }

    public void setDeviceTrusted(boolean deviceTrusted) {
        this.deviceTrusted = deviceTrusted;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
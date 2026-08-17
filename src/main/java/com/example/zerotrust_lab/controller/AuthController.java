package com.example.zerotrust_lab.controller;

import com.example.zerotrust_lab.dto.LoginRequest;
import com.example.zerotrust_lab.dto.RegisterRequest;
import com.example.zerotrust_lab.dto.VerifyOtpRequest;
import com.example.zerotrust_lab.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        try {

            String result =
                    authService.register(
                            request.getUsername(),
                            request.getPassword(),
                            request.getRole()
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            result
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        try {

            String result =
                    authService.login(
                            request.getUsername(),
                            request.getPassword()
                    );

            if (result.equals("MFA_REQUIRED")) {

                return ResponseEntity.ok(
                        Map.of(
                                "message",
                                "MFA required. Check application console for OTP."
                        )
                );
            }

            return ResponseEntity.ok(
                    Map.of(
                            "token",
                            result,
                            "message",
                            "Login successful"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody VerifyOtpRequest request) {

        try {

            String token =
                    authService.verifyOtp(
                            request.getUsername(),
                            request.getOtp()
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "token",
                            token,
                            "message",
                            "MFA verification successful"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }
}
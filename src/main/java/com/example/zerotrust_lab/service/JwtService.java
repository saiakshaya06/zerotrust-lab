package com.example.zerotrust_lab.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // ==========================================
    // JWT SECRET
    // ==========================================

    @Value("${JWT_SECRET:ZeroTrustLabSecretKeyForJWTAuthentication123456789}")
    private String secret;

    // ==========================================
    // CREATE SECRET KEY
    // ==========================================

    private SecretKey getKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ==========================================
    // GENERATE JWT TOKEN
    // ==========================================

    public String generateToken(
            String username,
            String role) {

        return Jwts.builder()

                .subject(username)

                .claim("role", role)

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 60 * 60 * 1000
                        )
                )

                .signWith(getKey())

                .compact();
    }

    // ==========================================
    // EXTRACT USERNAME
    // ==========================================

    public String extractUsername(
            String token) {

        return Jwts.parser()

                .verifyWith(getKey())

                .build()

                .parseSignedClaims(token)

                .getPayload()

                .getSubject();
    }

    // ==========================================
    // VALIDATE JWT
    // ==========================================

    public boolean isValid(
            String token) {

        try {

            Jwts.parser()

                    .verifyWith(getKey())

                    .build()

                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}
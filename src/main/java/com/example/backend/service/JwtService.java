// package com.example.backend.service;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.stereotype.Service;

// import javax.crypto.SecretKey;
// import java.util.Date;

// @Service
// public class JwtService {

//     private final String secret =
//             "mySecretKeyForFreelancerProject12345678901234567890";

//     private final long expiration = 1000 * 60 * 60; // 1 hour

//     private SecretKey getKey() {
//         return Keys.hmacShaKeyFor(secret.getBytes());
//     }

//     public String generateToken(String email, String role) {

//         return Jwts.builder()
//                 .subject(email)
//                 .claim("role", role)
//                 .issuedAt(new Date())
//                 .expiration(new Date(System.currentTimeMillis() + expiration))
//                 .signWith(getKey())
//                 .compact();
//     }
// }

package com.example.backend.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String secret =
            "mySecretKeyForFreelancerProject12345678901234567890";

    private final long expiration = 1000 * 60 * 60; // 1 hour

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, String role) {

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {

        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {

        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {

        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
package com.example.FazaaAI.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration; // In milliseconds

    // Generate a secure key from the secret
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT token
    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get userId from token
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Long.parseLong(claims.get("userId").toString());
    }

    // Get email (subject) from token
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token); // If this succeeds, the token is valid
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log specific exceptions if needed (e.g., ExpiredJwtException, MalformedJwtException)
            return false;
        }
    }

    // Helper method to extract claims
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
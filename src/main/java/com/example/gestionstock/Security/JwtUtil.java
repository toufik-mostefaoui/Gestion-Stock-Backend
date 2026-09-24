package com.example.gestionstock.Security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service

public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.refresh-secret}")
    private String refreshSecret;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    private SecretKey getRefreshKey() {
        return Keys.hmacShaKeyFor(
                refreshSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(String email, String role) {

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 1000L * 60 * 60)
                )
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getRefreshKey())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValid(String token){
        try{
            extractClaims(token);
            return true;
        }catch (Exception e){
            System.out.println("ACCESS JWT ERROR: " + e.getMessage());
            return false;
        }
    }

    public Claims extractRefreshClaims(String token) {

        return Jwts.parser()
                .verifyWith(getRefreshKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isRefreshTokenValid(String token) {

        try {
            extractRefreshClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("REFRESH JWT ERROR: " + e.getMessage());
            return false;
        }
    }

    public String getEmail(String token){
      return extractClaims(token).getSubject();
    };

    public String getRefreshEmail(String token) {
        return extractRefreshClaims(token).getSubject();
    }

    public String getRole(String token){
        return extractClaims(token).get("role" , String.class);
    }
}

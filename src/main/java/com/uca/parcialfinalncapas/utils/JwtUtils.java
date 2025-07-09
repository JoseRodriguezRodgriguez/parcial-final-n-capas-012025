package com.uca.parcialfinalncapas.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.JwtException;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration; // en milisegundos

    private Key key;

    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generar token con correo del usuario
    public String generateToken(Authentication auth) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(auth.getName()) // username
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validar token
    public boolean isTokenValid(String token) {
        try {
            extractUsername(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token inválido: {}", e.getMessage());
            return false;
        }
    }
}
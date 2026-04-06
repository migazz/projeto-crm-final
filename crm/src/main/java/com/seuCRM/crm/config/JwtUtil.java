package com.seuCRM.crm.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// @Component registra essa classe como um bean do Spring
@Component
public class JwtUtil {

    // Lê o valor do application.properties
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Gera a chave de assinatura a partir do secret
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Cria um novo token JWT para o usuário
    public String gerarToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email) // quem é o usuário
                .setIssuedAt(new Date()) // quando foi criado
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // quando expira
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // assina com a chave secreta
                .compact();
    }

    // Extrai o email do token
    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    // Verifica se o token ainda é válido
    public boolean tokenValido(String token, String email) {
        String emailDoToken = extrairEmail(token);
        return emailDoToken.equals(email) && !tokenExpirado(token);
    }

    // Verifica se o token já expirou
    private boolean tokenExpirado(String token) {
        return extrairClaims(token).getExpiration().before(new Date());
    }

    // Extrai todas as informações do token
    private Claims extrairClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
package com.hackthon.renegados.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY_USER = "segredo_muito_secreto_do_USER";
    private static final String SECRET_KEY_ADMIN = "segredo_muito_secreto_do_ADMIN";
    private static final String SECRET_KEY_PROF = "segredo_muito_secreto_do_PROF";
    private static final String SECRET_KEY_ALUNO = "segredo_muito_secreto_do_ALUNO";
    private static final long EXPIRATION_TIME_MS = 86400000;

    public String userToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .sign(Algorithm.HMAC256(SECRET_KEY_USER));
    }

    public String adminToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .sign(Algorithm.HMAC256(SECRET_KEY_ADMIN));
    }

    public String profToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .sign(Algorithm.HMAC256(SECRET_KEY_PROF));
    }

    public String alunoToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .sign(Algorithm.HMAC256(SECRET_KEY_ALUNO));
    }

    public String validateTokenAndGetUsername(String token) {
        Algorithm algorithm;

        if (token.contains("ROLE_ADMIN")) {
            algorithm = Algorithm.HMAC256(SECRET_KEY_ADMIN);
        } else if (token.contains("ROLE_PROF")) {
            algorithm = Algorithm.HMAC256(SECRET_KEY_PROF);
        } else if (token.contains("ROLE_ALUNO")) {
            algorithm = Algorithm.HMAC256(SECRET_KEY_ALUNO);
        } else {
            algorithm = Algorithm.HMAC256(SECRET_KEY_USER);
        }

        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }
}

package com.hackthon.renegados.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY_ADMIN = "segredo_muito_secreto_do_ADMIN";
    private static final String SECRET_KEY_PROF = "segredo_muito_secreto_do_PROF";
    private static final String SECRET_KEY_ALUNO = "segredo_muito_secreto_do_ALUNO";
    private static final long EXPIRATION_TIME_MS = 86400000;


    public String adminToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .sign(Algorithm.HMAC256(SECRET_KEY_ADMIN));
    }

    public String profToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .sign(Algorithm.HMAC256(SECRET_KEY_PROF));
    }

    public String alunoToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .sign(Algorithm.HMAC256(SECRET_KEY_ALUNO));
    }

    public String validateTokenAndGetUsername(String token) {

        Algorithm[] algorithms = new Algorithm[]{
                Algorithm.HMAC256(SECRET_KEY_ADMIN),
                Algorithm.HMAC256(SECRET_KEY_PROF),
                Algorithm.HMAC256(SECRET_KEY_ALUNO)
        };

        for (Algorithm algorithm : algorithms) {
            try {
                var verifier = JWT.require(algorithm).build();
                var decodedJWT = verifier.verify(token);
                // Se passou, retorna o subject
                return decodedJWT.getSubject();
            } catch (Exception e) {
                // tenta próxima chave
            }
        }

        throw new RuntimeException("Token inválido");
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            DecodedJWT decoded = JWT.decode(token); // decodifica sem validar
            String role = decoded.getClaim("role").asString();

            Algorithm algorithm;
            switch (role) {
                case "ADMIN":
                    algorithm = Algorithm.HMAC256(SECRET_KEY_ADMIN);
                    break;
                case "PROF":
                    algorithm = Algorithm.HMAC256(SECRET_KEY_PROF);
                    break;
                case "ALUNO":
                    algorithm = Algorithm.HMAC256(SECRET_KEY_ALUNO);
                    break;
                default:
                    return false;
            }

            var verifier = JWT.require(algorithm).build();
            var verifiedJWT = verifier.verify(token);

            String usernameFromToken = verifiedJWT.getSubject();
            Date expiration = verifiedJWT.getExpiresAt();

            return usernameFromToken.equals(userDetails.getUsername()) && expiration.after(new Date());

        } catch (JWTDecodeException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }


}

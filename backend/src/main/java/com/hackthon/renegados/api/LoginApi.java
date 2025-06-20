package com.hackthon.renegados.api;

import com.hackthon.renegados.api.dto.LoginRequest;
import com.hackthon.renegados.model.Usuario;
import com.hackthon.renegados.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hackthon.renegados.security.JwtUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class LoginApi {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        String token = null;

        try {

            // Autentica o usuário
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
            );

            // Pega o objeto do usuário ja autenticado
            Usuario usuario = (Usuario) authentication.getPrincipal();

            // Verifica a RULE desse usuário
            String role = usuario.getRole();

            // Gera o token dependen do tipo de rule que o usuário tem
            if (role == "ADMIN") {
                token = jwtUtil.adminToken(loginRequest.getLogin(), role);
            }else if (role == "PROF") {
                token = jwtUtil.adminToken(loginRequest.getLogin(), role);
            }else if (role == "ALUNO") {
                token = jwtUtil.adminToken(loginRequest.getLogin(), role);
            }else {
                token = jwtUtil.userToken(loginRequest.getLogin(), role);
            }


            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", role);

            return ResponseEntity.ok(response);


        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Credenciais inválidas"));
        }
    }
}

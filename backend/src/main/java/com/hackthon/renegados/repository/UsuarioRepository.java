package com.hackthon.renegados.repository;

import com.hackthon.renegados.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByLogin(String login);

    List<Usuario> findByRole(String role);
}

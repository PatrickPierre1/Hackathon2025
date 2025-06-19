package com.hackthon.renegados.service;



import com.hackthon.renegados.model.Usuario;
import com.hackthon.renegados.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }

    public UserDetails save(Usuario usuario) {
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }



    public Usuario editar(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        usuarioExistente.setLogin(usuarioAtualizado.getLogin());
        usuarioExistente.setRole(usuarioAtualizado.getRole());

        // Atualiza a senha apenas se vier uma nova (para não resetar a senha sem querer)
        if (usuarioAtualizado.getPassword() != null && !usuarioAtualizado.getPassword().isEmpty()) {
            usuarioExistente.setPassword(new BCryptPasswordEncoder().encode(usuarioAtualizado.getPassword()));
        }

        return repository.save(usuarioExistente);
    }

    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }


    public Usuario buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }

}

package com.hackthon.renegados.api;

import com.hackthon.renegados.model.Usuario;
import com.hackthon.renegados.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
public class AdminApi {

    @Autowired
    private UsuarioService usuarioService;

    // LISTAR TODOS OS ADMINs
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        List<Usuario> admins = usuarioService.listarPorRole("ADMIN");

        if (admins.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Nenhum admin encontrado"));
        }
        return ResponseEntity.ok(admins);
    }

    // BUSCAR ADMIN POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Usuario admin = usuarioService.buscarPorId(id);

        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Admin não encontrado com id: " + id));
        }
        return ResponseEntity.ok(admin);
    }

    // CADASTRAR NOVO ADMIN
    @PostMapping("/salvar")
    public ResponseEntity<?> adicionar(@RequestBody Usuario usuario) {
        try {
            usuario.setRole("ADMIN");
            Usuario salvo = (Usuario) usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Admin criado com sucesso", "admin", salvo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao criar admin: " + e.getMessage()));
        }
    }

    // ATUALIZAR ADMIN
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario existente = usuarioService.buscarPorId(id);
            if (existente == null || !"ADMIN".equals(existente.getRole())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Admin não encontrado com id: " + id));
            }

            usuario.setRole("ADMIN");
            Usuario atualizado = usuarioService.editar(id, usuario);
            return ResponseEntity.ok(Map.of("message", "Admin atualizado com sucesso", "admin", atualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao atualizar admin: " + e.getMessage()));
        }
    }

    // DELETAR ADMIN
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            Usuario existente = usuarioService.buscarPorId(id);
            if (existente == null || !"ADMIN".equals(existente.getRole())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Admin não encontrado com id: " + id));
            }

            usuarioService.deletarPorId(id);
            return ResponseEntity.ok(Map.of("message", "Admin deletado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao deletar admin: " + e.getMessage()));
        }
    }
}

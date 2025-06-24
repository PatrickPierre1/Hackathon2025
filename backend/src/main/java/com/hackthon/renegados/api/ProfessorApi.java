package com.hackthon.renegados.api;

import com.hackthon.renegados.model.Professor;
import com.hackthon.renegados.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/professor")
public class ProfessorApi {

    @Autowired
    private ProfessorService professorService;

    // LISTAR TODOS
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {

        if (professorService.listarTodos().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Nenhum professor encontrado"));
        }

        List<Professor> professores = professorService.listarTodos();
        if (professores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Nenhum professor encontrado"));
        }
        return ResponseEntity.ok(professores);
    }

    // BUSCAR POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        if (professorService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Professor nao encontrado com id: " + id));
        }

        try {
            Professor professor = professorService.buscarPorId(id);
            return ResponseEntity.ok(professor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Professor não encontrado com id: " + id));
        }
    }

    // CRIAR
    @PostMapping("/salvar")
    public ResponseEntity<?> adicionar(@RequestBody Professor professor) {
        try {
            Professor salvo = professorService.salvar(professor);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Professor criado com sucesso", "professor", salvo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao criar professor: " + e.getMessage()));
        }
    }

    // ATUALIZAR
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Professor professor) {

        if (professorService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Professor nao encontrado com id: " + id));
        }

        try {
            professor.setId(id);
            Professor atualizado = professorService.salvar(professor);
            return ResponseEntity.ok(Map.of("message", "Professor atualizado com sucesso", "professor", atualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao atualizar professor: " + e.getMessage()));
        }
    }

    // DELETAR
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        if (professorService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Professor nao encontrado com id: " + id));
        }

        try {
            professorService.deletarPorId(id);
            return ResponseEntity.ok(Map.of("message", "Professor deletado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Professor não encontrado com id: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao deletar professor: " + e.getMessage()));
        }
    }
}

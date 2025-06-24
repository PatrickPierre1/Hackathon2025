package com.hackthon.renegados.api;

import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map;

@RestController
@RequestMapping("api/aluno")
public class AlunoApi {

    @Autowired
    private AlunoService alunoService;

    // LISTAR TODOS
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {

        if (alunoService.listarTodos().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Nenhum professor encontrado"));
        }

        List<Aluno> alunos = alunoService.listarTodos();
        return ResponseEntity.ok(alunos);
    }

    // BUSCAR POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        if (alunoService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Aluno nao encontrado com id: " + id));
        }

        try {
            Aluno aluno = alunoService.buscarPorId(id);
            return ResponseEntity.ok(aluno);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Aluno não encontrado com id: " + id));
        }
    }

    // CRIAR
    @PostMapping("/salvar")
    public ResponseEntity<?> adicionar(@RequestBody Aluno aluno) {
        try {
            Aluno salvo = alunoService.salvar(aluno);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Aluno criado com sucesso", "aluno", salvo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao criar aluno: " + e.getMessage()));
        }
    }

    // ATUALIZAR
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Aluno aluno) {

        if (alunoService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Aluno nao encontrado com id: " + id));
        }

        try {
            aluno.setId(id);
            Aluno atualizado = alunoService.salvar(aluno);
            return ResponseEntity.ok(Map.of("message", "Aluno atualizado com sucesso", "aluno", atualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao atualizar aluno: " + e.getMessage()));
        }
    }

    // DELETAR
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        if (alunoService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Aluno nao encontrado com id: " + id));
        }

        try {
            alunoService.deletarPorId(id);
            return ResponseEntity.ok(Map.of("message", "Aluno deletado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Aluno não encontrado com id: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao deletar aluno: " + e.getMessage()));
        }
    }
}

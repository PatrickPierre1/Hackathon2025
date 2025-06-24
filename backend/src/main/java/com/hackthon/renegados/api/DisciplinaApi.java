package com.hackthon.renegados.api;

import com.hackthon.renegados.model.Disciplina;
import com.hackthon.renegados.model.Professor;
import com.hackthon.renegados.model.Usuario;
import com.hackthon.renegados.service.DisciplinaService;
import com.hackthon.renegados.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/disciplina")
public class DisciplinaApi {

    // DTO para Usuario (exclui senha)
    public record UsuarioDto(Long id, String nome, String login, String role) { }

    // DTO para Professor sem disciplinas
    public record ProfessorDto(Long id, Long cpf, UsuarioDto usuario) { }

    // DTO para Disciplina com professor
    public record DisciplinaDto(Long id, String nome, ProfessorDto professor) { }

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private ProfessorService professorService;

    private UsuarioDto toUsuarioDto(Usuario u) {
        if (u == null) return null;
        return new UsuarioDto(u.getId(), u.getNome(), u.getLogin(), u.getRole());
    }

    private ProfessorDto toProfessorDto(Professor p) {
        if (p == null) return null;
        var usuarioDto = toUsuarioDto(p);
        return new ProfessorDto(p.getId(), p.getCpf(), usuarioDto);
    }

    private DisciplinaDto toDisciplinaDto(Disciplina d) {
        if (d == null) return null;
        return new DisciplinaDto(d.getId(), d.getNome(), toProfessorDto(d.getProfessor()));
    }

    // LISTAR TODOS
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        List<Disciplina> disciplinas = disciplinaService.listarTodos();

        if (disciplinas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Nenhuma disciplina encontrada"));
        }

        List<DisciplinaDto> dtoList = disciplinas.stream()
                .map(this::toDisciplinaDto)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    // BUSCAR POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Disciplina disciplina = disciplinaService.buscarPorId(id);

        if (disciplina == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Disciplina não encontrada com id: " + id));
        }

        return ResponseEntity.ok(toDisciplinaDto(disciplina));
    }

    // CRIAR
    @PostMapping("/salvar")
    public ResponseEntity<?> adicionar(@RequestBody Disciplina disciplina) {
        try {
            Disciplina salvo = disciplinaService.salvar(disciplina);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Disciplina criada com sucesso",
                            "disciplinaId", salvo.getId()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao criar disciplina: " + e.getMessage()));
        }
    }

    // VINCULAR PROFESSOR EM UMA DISCIPLINA
    @PutMapping("/vincular-professor/{disciplinaId}/{professorId}")
    public ResponseEntity<?> vincularProfessor(@PathVariable Long disciplinaId, @PathVariable Long professorId) {
        Disciplina disciplina = disciplinaService.buscarPorId(disciplinaId);
        Professor professor = professorService.buscarPorId(professorId);

        if (disciplina == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Disciplina não encontrada com id: " + disciplinaId));
        }
        if (professor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Professor não encontrado com id: " + professorId));
        }

        try {
            disciplina.setProfessor(professor);
            Disciplina atualizado = disciplinaService.salvar(disciplina);
            return ResponseEntity.ok(Map.of(
                    "message", "Professor vinculado com sucesso",
                    "disciplina", toDisciplinaDto(atualizado)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao vincular professor: " + e.getMessage()));
        }
    }

    // ATUALIZAR DADOS DA DISCIPLINA
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Disciplina disciplina) {
        if (disciplinaService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Disciplina não encontrada com id: " + id));
        }

        try {
            disciplina.setId(id);
            Disciplina atualizado = disciplinaService.salvar(disciplina);
            return ResponseEntity.ok(Map.of(
                    "message", "Disciplina atualizada com sucesso",
                    "disciplina", toDisciplinaDto(atualizado)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao atualizar disciplina: " + e.getMessage()));
        }
    }

    // DELETAR
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (disciplinaService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Disciplina não encontrada com id: " + id));
        }

        try {
            disciplinaService.deletarPorId(id);
            return ResponseEntity.ok(Map.of("message", "Disciplina deletada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao deletar disciplina: " + e.getMessage()));
        }
    }
}

package com.hackthon.renegados.api;

import com.hackthon.renegados.model.*;
import com.hackthon.renegados.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/turma")
public class TurmaApi {

    // DTO para Usuario (sem senha)
    public record UsuarioDto(Long id, String nome, String login, String role) { }

    // DTO para Professor (sem disciplinas)
    public record ProfessorDto(Long id, Long cpf, UsuarioDto usuario) { }

    // DTO para Disciplina com professor
    public record DisciplinaDto(Long id, String nome, ProfessorDto professor) { }

    // DTO para TurmaDisciplina (entidade intermediária)
    public record TurmaDisciplinaDto(Long id, DisciplinaDto disciplina) { }

    // DTO para Turma com lista de TurmaDisciplinaDto
    public record TurmaDto(Long id, String nome, List<DisciplinaDto> turmaDisciplinas) { }

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private TurmaDisciplinaService turmaDisciplinaService;

    private UsuarioDto toUsuarioDto(Usuario u) {
        if (u == null) return null;
        return new UsuarioDto(u.getId(), u.getNome(), u.getLogin(), u.getRole());
    }

    private ProfessorDto toProfessorDto(Professor p) {
        if (p == null) return null;
        return new ProfessorDto(p.getId(), p.getCpf(), toUsuarioDto(p));
    }

    private DisciplinaDto toDisciplinaDto(Disciplina d) {
        if (d == null) return null;
        return new DisciplinaDto(d.getId(), d.getNome(), toProfessorDto(d.getProfessor()));
    }

    private TurmaDisciplinaDto toTurmaDisciplinaDto(TurmaDisciplina td) {
        if (td == null) return null;
        return new TurmaDisciplinaDto(td.getId(), toDisciplinaDto(td.getDisciplina()));
    }


    private TurmaDto toTurmaDto(Turma t) {
        List<DisciplinaDto> turmaDiscDtos = t.getTurmaDisciplinas().stream()
                .map(td -> toDisciplinaDto(td.getDisciplina()))
                .toList();

        return new TurmaDto(t.getId(), t.getNome(), turmaDiscDtos);
    }

    // LISTAR TODOS
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        List<Turma> turmas = turmaService.listarTodos();

        if (turmas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Nenhuma turma encontrada"));
        }

        List<TurmaDto> dtoList = turmas.stream()
                .map(this::toTurmaDto)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    // BUSCAR POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Turma turma = turmaService.buscarPorId(id);

        if (turma == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Turma não encontrada com id: " + id));
        }

        return ResponseEntity.ok(toTurmaDto(turma));
    }

    // CRIAR TURMA
    @PostMapping("/salvar")
    public ResponseEntity<?> adicionar(@RequestBody Turma turma) {
        try {
            Turma salvo = turmaService.salvar(turma);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Turma criada com sucesso",
                            "turma", toTurmaDto(salvo)
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao criar turma: " + e.getMessage()));
        }
    }

    // VINCULAR DISCIPLINA EM UMA TURMA (cria TurmaDisciplina)
    @PutMapping("/vincular-disciplina/{turmaId}/{disciplinaId}")
    public ResponseEntity<?> vincularDisciplina(@PathVariable Long turmaId, @PathVariable Long disciplinaId) {
        Turma turma = turmaService.buscarPorId(turmaId);
        Disciplina disciplina = disciplinaService.buscarPorId(disciplinaId);

        if (turma == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Turma não encontrada com id: " + turmaId));
        }
        if (disciplina == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Disciplina não encontrada com id: " + disciplinaId));
        }

        try {
            // Supondo que turmaDisciplinas é List<TurmaDisciplina>
            if (turma.getTurmaDisciplinas().stream().noneMatch(td -> td.getDisciplina().equals(disciplina))) {
                TurmaDisciplina td = new TurmaDisciplina();
                td.setTurma(turma);
                td.setDisciplina(disciplina);
                turma.getTurmaDisciplinas().add(td);
            }

            turmaService.salvar(turma);

            Turma atualizado = turmaService.buscarPorIdComDisciplinas(turmaId);

            return ResponseEntity.ok(Map.of(
                    "message", "Disciplina vinculada com sucesso",
                    "turma", toTurmaDto(atualizado)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao vincular disciplina: " + e.getMessage()));
        }
    }

    // ATUALIZAR DADOS DA TURMA
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Turma turma) {
        if (turmaService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Turma não encontrada com id: " + id));
        }

        try {
            turma.setId(id);
            Turma atualizado = turmaService.salvar(turma);
            return ResponseEntity.ok(Map.of(
                    "message", "Turma atualizada com sucesso",
                    "turma", toTurmaDto(atualizado)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao atualizar turma: " + e.getMessage()));
        }
    }

    // DELETAR TURMA
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (turmaService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Turma não encontrada com id: " + id));
        }

        try {
            turmaService.deletarPorId(id);
            return ResponseEntity.ok(Map.of("message", "Turma deletada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao deletar turma: " + e.getMessage()));
        }
    }
}

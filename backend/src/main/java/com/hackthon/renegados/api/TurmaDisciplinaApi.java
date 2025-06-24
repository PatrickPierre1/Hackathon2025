package com.hackthon.renegados.api;

import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.model.Provas;
import com.hackthon.renegados.model.TurmaDisciplina;
import com.hackthon.renegados.service.AlunoService;
import com.hackthon.renegados.service.ProvaService;
import com.hackthon.renegados.service.TurmaDisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/turma-disciplina")
public class TurmaDisciplinaApi {

    @Autowired
    private TurmaDisciplinaService turmaDisciplinaService;

    @Autowired
    private ProvaService provaService;

    @Autowired
    private AlunoService alunoService;


    // VINCULAR PROVA
    @PutMapping("/vincular-prova/{turmaId}/{disciplinaId}/{provaId}")
    public ResponseEntity<?> vincularProva(
            @PathVariable Long turmaId,
            @PathVariable Long disciplinaId,
            @PathVariable Long provaId
    ) {
        try {
            TurmaDisciplina turmaDisciplina = turmaDisciplinaService.buscarOuCriar(turmaId, disciplinaId);
            Provas prova = provaService.buscarPorId(provaId);

            if (prova == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Prova não encontrada com id: " + provaId));
            }

            // Verifica se já está vinculada (para evitar duplicidade)
            if (!turmaDisciplina.getProvas().contains(prova)) {
                turmaDisciplina.getProvas().add(prova);
                turmaDisciplinaService.salvar(turmaDisciplina);
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Prova vinculada com sucesso",
                    "turmaDisciplinaId", turmaDisciplina.getId(),
                    "provaId", prova.getId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao vincular prova: " + e.getMessage()));
        }
    }

    // LISTAR TODAS AS PROVAS DE TODAS AS TURMA_DISCIPLINA
    @GetMapping("/listar-provas")
    public ResponseEntity<?> listarTodasProvas() {
        List<TurmaDisciplina> lista = turmaDisciplinaService.listarTodos();

        List<Map<String, Object>> resultado = lista.stream().map(td -> Map.of(
                "turmaDisciplinaId", td.getId(),
                "turmaId", td.getTurma().getId(),
                "turmaNome", td.getTurma().getNome(),
                "disciplinaId", td.getDisciplina().getId(),
                "disciplinaNome", td.getDisciplina().getNome(),
                "provas", td.getProvas().stream().map(p -> Map.of(
                        "provaId", p.getId(),
                        "data", p.getData()
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }


    // LISTAR PROVAS FILTRADAS POR TURMA E DISCIPLINA
    @GetMapping("/listar-provas/{turmaId}/{disciplinaId}")
    public ResponseEntity<?> listarProvasPorTurmaDisciplina(
            @PathVariable Long turmaId,
            @PathVariable Long disciplinaId
    ) {
        try {
            TurmaDisciplina turmaDisciplina = turmaDisciplinaService.buscar(turmaId, disciplinaId);

            if (turmaDisciplina == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Relação turma e disciplina não encontrada"));
            }

            List<Map<String, Object>> provas = turmaDisciplina.getProvas().stream()
                    .map(p -> Map.of(
                            "provaId", (Object) p.getId(),
                            "data", (Object) p.getData()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "turmaDisciplinaId", turmaDisciplina.getId(),
                    "turmaId", turmaDisciplina.getTurma().getId(),
                    "turmaNome", turmaDisciplina.getTurma().getNome(),
                    "disciplinaId", turmaDisciplina.getDisciplina().getId(),
                    "disciplinaNome", turmaDisciplina.getDisciplina().getNome(),
                    "provas", provas
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao listar provas: " + e.getMessage()));
        }
    }


    // Vincular alunos a TurmaDisciplina
    @PutMapping("/vincular-alunos/{turmaId}/{disciplinaId}")
    public ResponseEntity<?> vincularAlunos(
            @PathVariable Long turmaId,
            @PathVariable Long disciplinaId,
            @RequestBody List<Long> alunosIds
    ) {
        try {
            TurmaDisciplina turmaDisciplina = turmaDisciplinaService.buscarOuCriar(turmaId, disciplinaId);

            List<Aluno> alunos = new ArrayList<>();
            for (Long alunoId : alunosIds) {
                Aluno aluno = alunoService.buscarPorId(alunoId);
                if (aluno == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "Aluno não encontrado com id: " + alunoId));
                }
                // Atualiza vínculo
                aluno.setTurmaDisciplina(turmaDisciplina);
                alunos.add(aluno);
            }
            alunoService.salvarTodos(alunos);

            // Atualiza lista local (opcional)
            turmaDisciplina.getAlunos().addAll(alunos);
            turmaDisciplinaService.salvar(turmaDisciplina);

            return ResponseEntity.ok(Map.of(
                    "message", "Alunos vinculados com sucesso",
                    "turmaDisciplinaId", turmaDisciplina.getId(),
                    "alunosVinculados", alunosIds
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao vincular alunos: " + e.getMessage()));
        }
    }

    // Listar alunos (todos)
    @GetMapping("/listar-alunos")
    public ResponseEntity<?> listarTodosAlunos() {
        List<TurmaDisciplina> lista = turmaDisciplinaService.listarTodos();

        List<Map<String, Object>> resultado = lista.stream().map(td -> Map.of(
                "turmaDisciplinaId", td.getId(),
                "turmaId", td.getTurma().getId(),
                "turmaNome", td.getTurma().getNome(),
                "disciplinaId", td.getDisciplina().getId(),
                "disciplinaNome", td.getDisciplina().getNome(),
                "alunos", td.getAlunos().stream().map(this::toAlunoMap).collect(Collectors.toList())
        )).collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    // Listar alunos por turma e disciplina
    @GetMapping("/listar-alunos-turma-disciplina/{turmaId}/{disciplinaId}")
    public ResponseEntity<?> listarAlunosPorTurmaDisciplina(
            @PathVariable Long turmaId,
            @PathVariable Long disciplinaId
    ) {
        try {
            TurmaDisciplina turmaDisciplina = turmaDisciplinaService.buscar(turmaId, disciplinaId);

            if (turmaDisciplina == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Relação turma e disciplina não encontrada"));
            }

            List<Map<String, Object>> alunos = turmaDisciplina.getAlunos().stream()
                    .map(this::toAlunoMap)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "turmaDisciplinaId", turmaDisciplina.getId(),
                    "turmaId", turmaDisciplina.getTurma().getId(),
                    "turmaNome", turmaDisciplina.getTurma().getNome(),
                    "disciplinaId", turmaDisciplina.getDisciplina().getId(),
                    "disciplinaNome", turmaDisciplina.getDisciplina().getNome(),
                    "alunos", alunos
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao listar alunos: " + e.getMessage()));
        }
    }

    // Conversor Aluno -> Map para retorno JSON
    private Map<String, Object> toAlunoMap(Aluno aluno) {
        return Map.of(
                "id", aluno.getId(),
                "nome", aluno.getNome(),
                "login", aluno.getLogin(),
                "ra", aluno.getRa(),
                "cpf", aluno.getCpf(),
                "telefone", aluno.getTelefone(),
                "email", aluno.getEmail()
        );
    }
}
package com.hackthon.renegados.api;

import com.hackthon.renegados.api.dto.AlunoProvaInput;
import com.hackthon.renegados.model.AlunoProva;
import com.hackthon.renegados.service.AlunoProvaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/aluno-prova")
public class AlunoProvaApi {

    @Autowired
    private AlunoProvaService alunoProvaService;

    @PostMapping("/vincular")
    public ResponseEntity<?> vincularAlunoProva(@RequestBody AlunoProvaInput input) {
        try {
            AlunoProva salvo = alunoProvaService.salvarAlunoProvaComRespostas(input);
            return ResponseEntity.ok(Map.of(
                    "message", "Aluno-Prova vinculada com sucesso",
                    "id", salvo.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao vincular aluno-prova: " + e.getMessage()));
        }
    }

    @GetMapping("/listar/aluno/{alunoId}/turma/{turmaId}")
    public ResponseEntity<?> listarProvasAlunoTurma(
            @PathVariable Long alunoId,
            @PathVariable Long turmaId
    ) {
        try {
            List<AlunoProva> lista = alunoProvaService.listarPorAlunoETurma(alunoId, turmaId);

            List<Map<String, Object>> resultado = lista.stream().map(ap -> Map.of(
                    "alunoId", ap.getAluno().getId(),
                    "provaId", ap.getProva().getId(),
                    "turmaDisciplinaId", ap.getTurmaDisciplina().getId(),
                    "nota", ap.getNota(),
                    "status", ap.getStatus(),
                    "respostas", ap.getRespostas().stream().map(resp -> Map.of(
                            "numeroQuestao", resp.getQuestao().getNumeroQuestao(),
                            "respostaAluno", resp.getRespostaAluno()
                    )).toList()
            )).toList();

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao listar provas do aluno: " + e.getMessage()));
        }
    }


    @GetMapping("/{alunoId}/{provaId}")
    public ResponseEntity<?> buscarAlunoProva(
            @PathVariable Long alunoId,
            @PathVariable Long provaId
    ) {
        try {
            AlunoProva alunoProva = alunoProvaService.buscarPorAlunoEProva(alunoId, provaId);

            if (alunoProva == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Aluno-Prova não encontrada para o aluno " + alunoId + " e prova " + provaId));
            }

            Map<String, Object> response = Map.of(
                    "alunoId", alunoProva.getAluno().getId(),
                    "provaId", alunoProva.getProva().getId(),
                    "turmaDisciplinaId", alunoProva.getTurmaDisciplina().getId(),
                    "nota", alunoProva.getNota(),
                    "status", alunoProva.getStatus(),
                    "respostas", alunoProva.getRespostas().stream().map(r -> Map.of(
                            "numeroQuestao", r.getQuestao().getNumeroQuestao(),
                            "respostaAluno", r.getRespostaAluno()
                    )).toList()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao buscar aluno-prova: " + e.getMessage()));
        }
    }


    @GetMapping("/listar")
    public ResponseEntity<?> listarTodos() {
        return ResponseEntity.ok(alunoProvaService.listarTodos());
    }

    @GetMapping("/listar/prova/{provaId}")
    public ResponseEntity<?> listarPorProva(@PathVariable Long provaId) {
        return ResponseEntity.ok(alunoProvaService.listarPorProva(provaId));
    }

    @GetMapping("/listar/turma-disciplina/{turmaDisciplinaId}")
    public ResponseEntity<?> listarPorTurmaDisciplina(@PathVariable Long turmaDisciplinaId) {
        return ResponseEntity.ok(alunoProvaService.listarPorTurmaDisciplina(turmaDisciplinaId));
    }

    @GetMapping("/listar/aluno/{alunoId}")
    public ResponseEntity<?> listarPorAluno(@PathVariable Long alunoId) {
        return ResponseEntity.ok(alunoProvaService.listarPorAluno(alunoId));
    }

}

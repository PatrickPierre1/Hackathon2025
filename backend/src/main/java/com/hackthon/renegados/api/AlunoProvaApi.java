package com.hackthon.renegados.api;

import com.hackthon.renegados.model.AlunoProva;
import com.hackthon.renegados.service.AlunoProvaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/aluno-prova")
public class AlunoProvaApi {

    @Autowired
    private AlunoProvaService alunoProvaService;

    @PostMapping("/vincular")
    public ResponseEntity<?> vincularAlunoProva(@RequestBody AlunoProva alunoProva) {
        try {
            AlunoProva salvo = alunoProvaService.salvar(alunoProva);
            return ResponseEntity.ok(Map.of(
                    "message", "Aluno-Prova vinculada com sucesso",
                    "id", salvo.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao vincular aluno-prova: " + e.getMessage()));
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

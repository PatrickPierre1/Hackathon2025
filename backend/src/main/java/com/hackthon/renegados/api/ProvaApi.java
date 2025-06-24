package com.hackthon.renegados.api;

import com.hackthon.renegados.model.Disciplina;
import com.hackthon.renegados.model.Provas;
import com.hackthon.renegados.model.Questao;
import com.hackthon.renegados.service.DisciplinaService;
import com.hackthon.renegados.service.ProvaService;
import com.hackthon.renegados.service.QuestaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/prova")
public class ProvaApi {

    // DTO para Questao
    public record QuestaoDto(Long id, String numeroQuestao, String resposta) {}

    // DTO para Disciplina
    public record DisciplinaDto(Long id, String nome) {}

    // DTO para Prova
    public record ProvaDto(Long id, Date data, DisciplinaDto disciplina, List<QuestaoDto> questoes) {}

    @Autowired
    private ProvaService provaService;

    @Autowired
    private QuestaoService questaoService;

    @Autowired
    private DisciplinaService disciplinaService;

    private QuestaoDto toQuestaoDto(Questao q) {
        if (q == null) return null;
        return new QuestaoDto(q.getId(), q.getNumeroQuestao(), q.getResposta());
    }

    private DisciplinaDto toDisciplinaDto(Disciplina d) {
        if (d == null) return null;
        return new DisciplinaDto(d.getId(), d.getNome());
    }

    private ProvaDto toProvaDto(Provas p) {
        List<QuestaoDto> questoes = p.getQuestoes() == null ? List.of() :
                p.getQuestoes().stream()
                        .map(this::toQuestaoDto)
                        .collect(Collectors.toList());

        return new ProvaDto(p.getId(), p.getData(), toDisciplinaDto(p.getDisciplina()), questoes);
    }

    // LISTAR TODOS
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        List<Provas> provas = provaService.listarTodos();

        if (provas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Nenhuma prova encontrada"));
        }

        List<ProvaDto> dtoList = provas.stream()
                .map(this::toProvaDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    // BUSCAR POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Provas prova = provaService.buscarPorId(id);

        if (prova == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Prova não encontrada com id: " + id));
        }

        return ResponseEntity.ok(toProvaDto(prova));
    }

    // CRIAR PROVA
    @PostMapping("/salvar")
    public ResponseEntity<?> adicionar(@RequestBody ProvaDto provaDto) {
        try {
            if (provaDto.disciplina() == null || provaDto.disciplina().id() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Disciplina da prova é obrigatória"));
            }

            Disciplina disciplina = disciplinaService.buscarPorId(provaDto.disciplina().id());
            if (disciplina == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Disciplina não encontrada com id: " + provaDto.disciplina().id()));
            }

            // Cria e salva a prova
            Provas prova = new Provas();
            prova.setData(provaDto.data());
            prova.setDisciplina(disciplina);

            Provas salvaProva = provaService.salvar(prova);

            // Cria e salva as questões
            List<Questao> questoes = provaDto.questoes() == null ? List.of() :
                    provaDto.questoes().stream().map(dto -> {
                        Questao q = new Questao();
                        q.setNumeroQuestao(dto.numeroQuestao());
                        q.setResposta(dto.resposta());
                        q.setProva(salvaProva);
                        return q;
                    }).toList();

            questoes.forEach(questaoService::salvar);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Prova criada com sucesso",
                            "prova", new ProvaDto(
                                    salvaProva.getId(),
                                    salvaProva.getData(),
                                    new DisciplinaDto(disciplina.getId(), disciplina.getNome()),
                                    provaDto.questoes() == null ? List.of() : provaDto.questoes()
                            )
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao criar prova: " + e.getMessage()));
        }
    }

    // ATUALIZAR PROVA
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ProvaDto provaDto) {
        Provas provaExistente = provaService.buscarPorIdComQuestoes(id);
        if (provaExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Prova não encontrada com id: " + id));
        }

        try {
            // Atualiza data
            provaExistente.setData(provaDto.data());

            // Atualiza disciplina se enviada
            if (provaDto.disciplina() != null && provaDto.disciplina().id() != null) {
                Disciplina disciplina = disciplinaService.buscarPorId(provaDto.disciplina().id());
                if (disciplina == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "Disciplina não encontrada com id: " + provaDto.disciplina().id()));
                }
                provaExistente.setDisciplina(disciplina);
            }

            // Limpa questões antigas
            provaExistente.getQuestoes().clear();

            // Adiciona questões novas
            List<Questao> novasQuestoes = provaDto.questoes() == null ? List.of() :
                    provaDto.questoes().stream().map(dto -> {
                        Questao q = new Questao();
                        q.setId(dto.id());
                        q.setNumeroQuestao(dto.numeroQuestao());
                        q.setResposta(dto.resposta());
                        q.setProva(provaExistente);
                        return q;
                    }).toList();

            provaExistente.getQuestoes().addAll(novasQuestoes);

            Provas atualizado = provaService.salvar(provaExistente);

            return ResponseEntity.ok(Map.of(
                    "message", "Prova atualizada com sucesso",
                    "prova", new ProvaDto(
                            atualizado.getId(),
                            atualizado.getData(),
                            new DisciplinaDto(
                                    atualizado.getDisciplina().getId(),
                                    atualizado.getDisciplina().getNome()
                            ),
                            provaDto.questoes() == null ? List.of() : provaDto.questoes()
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao atualizar prova: " + e.getMessage()));
        }
    }


    // DELETAR PROVA
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (provaService.buscarPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Prova não encontrada com id: " + id));
        }

        try {
            provaService.deletarPorId(id);
            return ResponseEntity.ok(Map.of("message", "Prova deletada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao deletar prova: " + e.getMessage()));
        }
    }
}

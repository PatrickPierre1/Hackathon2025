package com.hackthon.renegados.controller;

import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.model.Disciplina;
import com.hackthon.renegados.model.Turma;
import com.hackthon.renegados.model.TurmaDisciplina;
import com.hackthon.renegados.service.AlunoService;
import com.hackthon.renegados.service.DisciplinaService;
import com.hackthon.renegados.service.TurmaDisciplinaService;
import com.hackthon.renegados.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/turma-disciplinas")
public class TurmaDisciplinaController {

    @Autowired
    private TurmaDisciplinaService turmaDisciplinaService;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private DisciplinaService disciplinaService;

    // Listar todos os vínculos
    @GetMapping("/listar")
    public String listar(Model model) {
        List<TurmaDisciplina> vinculos = turmaDisciplinaService.listarTodos();
        model.addAttribute("vinculos", vinculos);
        return "pages/turmaDisciplina/listagem";
    }


    // Visualizar vínculo
    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        TurmaDisciplina vinculo = turmaDisciplinaService.buscarPorId(id)
                .orElse(null);

        if (vinculo == null) {
            return "redirect:/turma-disciplinas/listar";
        }

        model.addAttribute("vinculo", vinculo);
        return "pages/turmaDisciplina/visualizar"; // Crie essa página com detalhes
    }

    // Exibir formulário para criar vínculo
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("turmas", turmaService.listarTodos());
        model.addAttribute("disciplinas", disciplinaService.listarTodos());
        return "pages/turmaDisciplina/formulario";
    }

    // Criar vínculo
    @PostMapping("/salvar")
    public String salvar(@RequestParam Long turmaId, @RequestParam Long disciplinaId) {
        Turma turma = turmaService.buscarPorId(turmaId);
        Disciplina disciplina = disciplinaService.buscarPorId(disciplinaId);

        if (turma != null && disciplina != null) {
            turmaDisciplinaService.criarVinculo(turma, disciplina);
        }

        return "redirect:/turma-disciplinas/listar";
    }

    // Deletar vínculo
    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        turmaDisciplinaService.deletarPorId(id);
        return "redirect:/turma-disciplinas/listar";
    }

    // Exibir formulário para vincular aluno
    @GetMapping("/vincular-aluno/{id}")
    public String exibirFormVincularAluno(@PathVariable Long id, Model model) {
        // Carregar o vínculo pelo ID
        TurmaDisciplina vinculo = turmaDisciplinaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Vínculo não encontrado"));

        // Carregar todos os alunos (para montar o Select)
        List<Aluno> alunos = alunoService.listarTodos();

        model.addAttribute("vinculo", vinculo);
        model.addAttribute("alunos", alunos);
        return "pages/turmaDisciplina/vincularAluno";
    }


    // Salvar o vínculo aluno + turmaDisciplina
    @PostMapping("/vincular-aluno/{id}")
    public String salvarVinculoAluno(
            @PathVariable Long id,
            @RequestParam("alunoId") Long alunoId
    ) {
        turmaDisciplinaService.vincularAluno(id, alunoId);
        return "redirect:/turma-disciplinas/visualizar/" + id;
    }


}

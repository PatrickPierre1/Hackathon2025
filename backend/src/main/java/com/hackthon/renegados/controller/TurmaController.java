package com.hackthon.renegados.controller;

import com.hackthon.renegados.model.*;
import com.hackthon.renegados.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private TurmaDisciplinaService turmaDisciplinaService;

    // Listar turmas
    @GetMapping("/listar")
    public String listarTurmas(Model model) {
        List<Turma> turmas = turmaService.listarTodos();
        model.addAttribute("turmas", turmas);
        return "pages/turma/listagemTurma"; // Thymeleaf page: src/main/resources/templates/turma/listar.html
    }

    // Exibir formulário de criação
    @GetMapping("/nova")
    public String novaTurma(Model model) {
        model.addAttribute("turma", new Turma());
        return "pages/turma/formularioCadastro"; // Thymeleaf page for form
    }

    // Salvar turma
    @PostMapping("/salvar")
    public String salvarTurma(@ModelAttribute Turma turma) {
        turmaService.salvar(turma);
        return "redirect:/turmas/listar";
    }

    // Exibir formulário de edição
    @GetMapping("/editar/{id}")
    public String editarTurma(@PathVariable Long id, Model model) {
        Turma turma = turmaService.buscarPorId(id);
        if (turma == null) {
            return "redirect:/turmas";
        }
        model.addAttribute("turma", turma);
        return "pages/turma/formularioEdicao";
    }

    // Deletar turma
    @GetMapping("/remover/{id}")
    public String removerTurma(@PathVariable Long id) {
        turmaService.deletarPorId(id);
        return "redirect:/turmas/listar";
    }

    // Vincular disciplina
    @GetMapping("/vincular/{id}")
    public String vincularDisciplinaForm(@PathVariable Long id, Model model) {
        Turma turma = turmaService.buscarPorId(id);
        List<Disciplina> disciplinas = disciplinaService.listarTodos();
        model.addAttribute("turma", turma);
        model.addAttribute("disciplinas", disciplinas);
        return "turma/vincular"; // Page to select discipline
    }

    @PostMapping("/vincular")
    public String vincularDisciplina(@RequestParam Long turmaId, @RequestParam Long disciplinaId) {
        Turma turma = turmaService.buscarPorId(turmaId);
        Disciplina disciplina = disciplinaService.buscarPorId(disciplinaId);

        if (turma != null && disciplina != null) {
            if (turma.getTurmaDisciplinas().stream().noneMatch(td -> td.getDisciplina().equals(disciplina))) {
                TurmaDisciplina td = new TurmaDisciplina();
                td.setTurma(turma);
                td.setDisciplina(disciplina);
                turma.getTurmaDisciplinas().add(td);
                turmaService.salvar(turma);
            }
        }

        return "redirect:/turmas";
    }
}

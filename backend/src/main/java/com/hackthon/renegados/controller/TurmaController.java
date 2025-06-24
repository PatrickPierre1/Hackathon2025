package com.hackthon.renegados.controller;

import com.hackthon.renegados.model.*;
import com.hackthon.renegados.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/turma")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private TurmaDisciplinaService turmaDisciplinaService;

    // LISTAR
    @GetMapping("/listar")
    public String listar(Model model) {
        List<Turma> turmas = turmaService.listarTodos();
        model.addAttribute("turmas", turmas);
        return "pages/turma/listagemTurmas";
    }

    // FORM ADICIONAR
    @GetMapping("/adicionar")
    public String adicionarForm(Model model) {
        model.addAttribute("turma", new Turma());
        return "turma/form"; // Exemplo: form.html
    }

    // SALVAR NOVA TURMA
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Turma turma) {
        turmaService.salvar(turma);
        return "redirect:/turma/listar";
    }

    // FORM EDITAR
    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Turma turma = turmaService.buscarPorId(id);
        if (turma == null) {
            return "redirect:/turma/listar";
        }
        model.addAttribute("turma", turma);
        return "turma/form";
    }

    // ATUALIZAR TURMA
    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Turma turma) {
        turma.setId(id);
        turmaService.salvar(turma);
        return "redirect:/turma/listar";
    }

    // REMOVER TURMA
    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        turmaService.deletarPorId(id);
        return "redirect:/turma/listar";
    }

    // DETALHE DE UMA TURMA
    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Turma turma = turmaService.buscarPorIdComDisciplinas(id);
        if (turma == null) {
            return "redirect:/turma/listar";
        }
        model.addAttribute("turma", turma);
        return "turma/detalhes";
    }

    // FORM PARA VINCULAR DISCIPLINA
    @GetMapping("/{id}/vincular-disciplina")
    public String formVincularDisciplina(@PathVariable Long id, Model model) {
        Turma turma = turmaService.buscarPorId(id);
        List<Disciplina> disciplinas = disciplinaService.listarTodos();
        model.addAttribute("turma", turma);
        model.addAttribute("disciplinas", disciplinas);
        return "turma/vincular-disciplina";
    }

    // SALVAR VINCULO
    @PostMapping("/{id}/vincular-disciplina")
    public String vincularDisciplina(@PathVariable Long id, @RequestParam Long disciplinaId) {
        Turma turma = turmaService.buscarPorId(id);
        Disciplina disciplina = disciplinaService.buscarPorId(disciplinaId);

        if (turma != null && disciplina != null) {
            boolean exists = turma.getTurmaDisciplinas().stream()
                    .anyMatch(td -> td.getDisciplina().equals(disciplina));
            if (!exists) {
                TurmaDisciplina td = new TurmaDisciplina();
                td.setTurma(turma);
                td.setDisciplina(disciplina);
                turma.getTurmaDisciplinas().add(td);
                turmaService.salvar(turma);
            }
        }

        return "redirect:/turma/" + id;
    }
}

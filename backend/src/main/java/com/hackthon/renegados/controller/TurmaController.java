package com.hackthon.renegados.controller;

import com.hackthon.renegados.api.dto.TurmaFormDto;
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
        return "pages/turma/listagemTurma";
    }

    // Exibir formulário de criação
    @GetMapping("/nova")
    public String novo(Model model) {
        model.addAttribute("turmaFormDto", new TurmaFormDto());
        model.addAttribute("disciplinas", disciplinaService.listarTodos());
        return "pages/turma/formularioCadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute TurmaFormDto turmaFormDto) {
        Turma turma = new Turma();
        turma.setNome(turmaFormDto.getNome());
        turmaService.salvar(turma);

        for (Long disciplinaId : turmaFormDto.getDisciplinasIds()) {
            Disciplina disciplina = disciplinaService.buscarPorId(disciplinaId);
            turmaDisciplinaService.criarVinculo(turma, disciplina);
        }

        return "redirect:/turmas/listar";
    }


    // Exibir formulário de edição
    @GetMapping("/editar/{id}")
    public String editarTurma(@PathVariable Long id, Model model) {
        Turma turma = turmaService.buscarPorId(id);

        if (turma == null) {
            return "redirect:/turmas";
        }

        // Instancia o DTO
        TurmaFormDto dto = new TurmaFormDto();
        dto.setId(turma.getId());
        dto.setNome(turma.getNome());

        // Pega as disciplinas já vinculadas a essa turma
        List<Long> disciplinasIds = turmaDisciplinaService.buscarDisciplinasIdsPorTurma(turma.getId());
        dto.setDisciplinasIds(disciplinasIds);

        // Adiciona no model
        model.addAttribute("turmaFormDto", dto);
        model.addAttribute("disciplinas", disciplinaService.listarTodos());

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
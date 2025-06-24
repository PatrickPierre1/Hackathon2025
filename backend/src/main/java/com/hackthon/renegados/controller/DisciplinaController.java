package com.hackthon.renegados.controller;

import com.hackthon.renegados.model.Disciplina;
import com.hackthon.renegados.model.Professor;
import com.hackthon.renegados.service.DisciplinaService;
import com.hackthon.renegados.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private ProfessorService professorService;

    // LISTAR TODAS AS DISCIPLINAS
    @GetMapping("/listar")
    public String listar(Model model) {
        List<Disciplina> disciplinas = disciplinaService.listarTodos();
        model.addAttribute("disciplinas", disciplinas);
        return "pages/disciplina/listagemDisciplina"; // View: resources/templates/disciplina/listar.html
    }

    // VISUALIZAR DETALHES DE UMA DISCIPLINA
    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        Disciplina disciplina = disciplinaService.buscarPorId(id);
        if (disciplina == null) {
            return "redirect:/disciplina/listar";
        }
        model.addAttribute("disciplina", disciplina);
        return "disciplina/visualizar"; // View: resources/templates/disciplina/visualizar.html
    }

    // FORMULÁRIO PARA NOVA DISCIPLINA
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("disciplina", new Disciplina());
        model.addAttribute("professores", professorService.listarTodos());
        return "pages/disciplina/formularioCadastro"; // View para criar nova disciplina
    }

    // FORMULÁRIO DE EDIÇÃO
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Disciplina disciplina = disciplinaService.buscarPorId(id);
        if (disciplina == null) {
            return "redirect:/disciplina/listar";
        }
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("professores", professorService.listarTodos());
        return "pages/disciplina/formularioEdicao";

    }

    // SALVAR (CRIAR ou ATUALIZAR)
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Disciplina disciplina) {
        disciplinaService.salvar(disciplina);
        return "redirect:/disciplina/listar";
    }

    // DELETAR
    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        disciplinaService.deletarPorId(id);
        return "redirect:/disciplina/listar";
    }

    // VINCULAR PROFESSOR
    @PostMapping("/vincular")
    public String vincularProfessor(@RequestParam Long disciplinaId, @RequestParam Long professorId) {
        Disciplina disciplina = disciplinaService.buscarPorId(disciplinaId);
        Professor professor = professorService.buscarPorId(professorId);

        if (disciplina != null && professor != null) {
            disciplina.setProfessor(professor);
            disciplinaService.salvar(disciplina);
        }
        return "redirect:/disciplina/visualizar/" + disciplinaId;
    }

}
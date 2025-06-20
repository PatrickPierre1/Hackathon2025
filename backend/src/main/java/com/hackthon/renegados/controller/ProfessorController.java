package com.hackthon.renegados.controller;

import com.hackthon.renegados.model.Professor;
import com.hackthon.renegados.model.Usuario;
import com.hackthon.renegados.repository.ProfessorRepository;
import com.hackthon.renegados.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private ProfessorService professorService;

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("professores", professorService.listarTodos());
        return "/pages/professor/listagemProfessor";
    }

    // Abrir Formulário, retorna o template
    @GetMapping("/abrirFormulario")
    public String abrirFormulario(Model model) {
        model.addAttribute("professor", new Professor());
        return "/pages/professor/formulatorioCadastro";
    }

    // Salvar Usuário e redireciona para home
    @PostMapping("/salvar")
    public String salvar(Model model, Professor professor) {
        professorService.save(professor);
        return "redirect:/professor/listar";
    }


    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        professorService.deletarPorId(id);
        return "redirect:/professor/listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Professor professor = professorService.buscarPorId(id);
        model.addAttribute("professor", professor);
        return "/pages/professor/formularioEdicao";
    }

}

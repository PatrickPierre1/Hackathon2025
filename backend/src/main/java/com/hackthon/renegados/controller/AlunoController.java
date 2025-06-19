package com.hackthon.renegados.controller;

import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("aluno")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) { this.alunoService = alunoService; }


    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        return "/pages/aluno/listagemAlunos";
    }

    // Abrir Formulário, retorna o template
    @GetMapping("/abrirFormulario")
    public String abrirFormulario() {
        System.out.println("Abrindo formulário de cadastro");
        return "/pages/aluno/formularioCadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("aluno") Aluno aluno) {
        alunoService.salvar(aluno);
        return "redirect:/aluno/listar";
    }

}

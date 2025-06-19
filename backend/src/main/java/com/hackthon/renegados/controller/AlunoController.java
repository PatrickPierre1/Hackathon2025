package com.hackthon.renegados.controller;

import com.hackthon.renegados.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}

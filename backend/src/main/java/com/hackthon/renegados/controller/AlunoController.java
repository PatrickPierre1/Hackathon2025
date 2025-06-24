package com.hackthon.renegados.controller;

import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.service.AlunoService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("aluno")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }


    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        return "/pages/aluno/listagemAlunos";
    }

    // Abrir Formulário, retorna o template
    @GetMapping("/abrirFormulario")
    public String abrirFormulario(Model model) {
        System.out.println("Abrindo formulário de cadastro");
        model.addAttribute("aluno", new Aluno());
        return "/pages/aluno/formularioCadastro";
    }

    // Abror Formulario edicao
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Aluno aluno = alunoService.buscarPorId(id);
        model.addAttribute("aluno", aluno);
        return "/pages/aluno/formularioEdicao";
    }


    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("aluno") Aluno aluno) {

        alunoService.salvar(aluno);
        return "redirect:/aluno/listar";
    }

    //Deletar aluno por id
    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        alunoService.deletarPorId(id);
        return "redirect:/aluno/listar";
    }

}

package com.hackthon.renegados.controller;

import com.hackthon.renegados.model.Disciplina;
import com.hackthon.renegados.model.Provas;
import com.hackthon.renegados.model.Questao;
import com.hackthon.renegados.service.DisciplinaService;
import com.hackthon.renegados.service.ProvaService;
import com.hackthon.renegados.service.QuestaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/provas")
public class ProvaController {

    @Autowired
    private ProvaService provaService;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private QuestaoService questaoService;

    // Listar todas as provas
    @GetMapping("/listar")
    public String listar(Model model) {
        List<Provas> provas = provaService.listarTodos();
        model.addAttribute("provas", provas);
        return "pages/prova/listagemProva"; // templates/prova/listar.html
    }

    // Formulário para criar nova prova
    @GetMapping("/nova")
    public String novaProvaForm(Model model) {
        model.addAttribute("prova", new Provas());
        model.addAttribute("disciplinas", disciplinaService.listarTodos());
        return "pages/prova/formularioCadastro"; // templates/prova/form.html
    }

    // Editar uma prova existente
    @GetMapping("/editar/{id}")
    public String editarProvaForm(@PathVariable Long id, Model model) {
        Provas prova = provaService.buscarPorIdComQuestoes(id);
        if (prova == null) {
            return "redirect:/provas";
        }
        model.addAttribute("prova", prova);
        model.addAttribute("disciplinas", disciplinaService.listarTodos());
        return "pages/prova/formularioEdicao"; // reutiliza o mesmo form
    }

    // Salvar nova ou editar existente
    @PostMapping("/salvar")
    public String salvarProva(@ModelAttribute Provas prova) {
        // Relaciona a disciplina
        if (prova.getDisciplina() != null && prova.getDisciplina().getId() != null) {
            Disciplina disciplina = disciplinaService.buscarPorId(prova.getDisciplina().getId());
            prova.setDisciplina(disciplina);
        }

        // Garante vínculo das questões
        if (prova.getQuestoes() != null) {
            for (Questao questao : prova.getQuestoes()) {
                questao.setProva(prova);
            }
        }

        provaService.salvar(prova);
        return "redirect:/provas/listar";
    }

    // Remover prova
    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        provaService.deletarPorId(id);
        return "redirect:/provas/listar";
    }
}

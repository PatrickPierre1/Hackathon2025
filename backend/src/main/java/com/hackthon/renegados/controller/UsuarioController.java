package com.hackthon.renegados.controller;


import com.hackthon.renegados.model.Usuario;
import com.hackthon.renegados.service.UsuarioService;
import com.hackthon.renegados.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // Abrir Formulário, retorna o template
    @GetMapping("/abrirFormulario")
    public String abrirFormulario() {
        System.out.println("Abrindo formulário de cadastro");
        return "/pages/usuario/formularioCadastro";
    }


    // Salvar Usuário e redireciona para home
    @PostMapping("/salvar")
    public String salvar(Usuario usuario) {
        service.save(usuario);
        return "redirect:/usuario/listar";
    }


    // Listar Usuários, retorna o template
    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("usuarios", service.listarTodos());
        return "/pages/usuario/listagemUsuarios";
    }

    // Editar Usuário, retorna o template com o usuário carregado
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = service.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "/pages/usuario/formularioEdicao";
    }



    // Remover Usuário, remove o usuário
    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        service.deletarPorId(id);
        return "redirect:/usuario/listar";
    }

}

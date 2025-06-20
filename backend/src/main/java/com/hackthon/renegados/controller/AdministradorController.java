package com.hackthon.renegados.controller;

import com.hackthon.renegados.service.AdministradorService;
import com.hackthon.renegados.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("administrador")
public class AdministradorController {

    @Autowired
    public AdministradorService administradorService;

//    @GetMapping("/listar")
//    public String listar(Model model) {
//        model.addAttribute("administradores", administradorService.listarTodos());
//        return "/pages/administrador/listagemAdministrador";
//    }

}

package com.hackthon.renegados.api;

import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.service.AlunoService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/aluno")
public class AlunoApi {


    @Autowired
    private AlunoService alunoService;


    @GetMapping("/listar")
    public List<Aluno> listar(){
        return alunoService.listarTodos();
    }


    @PostMapping("/salvar")
    public Aluno adinionar(@RequestBody Aluno aluno){
        return alunoService.salvar(aluno);
    }

}

package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService{

    @Autowired
    private AlunoRepository alunoRepository;

    // Listar alunos é uma lista de todos os alunos
    public List<Aluno>  listarTodos(){ return alunoRepository.findAll(); }


    public Aluno salvar(Aluno aluno){
        System.out.println("to aqui mano :"+aluno);
        try{
            if (aluno == null){
                throw  new RuntimeException("errou");
            }

            alunoRepository.save(aluno);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return aluno;
    }
}

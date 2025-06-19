package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Administrador;
import com.hackthon.renegados.model.Professor;
import com.hackthon.renegados.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public List<Professor> listarTodos(){ return professorRepository.findAll(); }

    public Professor salvar(Professor professor){
        System.out.println("to aqui mano :"+professor);
        try{
            if (professor == null){
                throw  new RuntimeException("errou");
            }

            professorRepository.save(professor);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return professor;
    }
}

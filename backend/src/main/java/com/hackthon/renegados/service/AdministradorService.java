package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Administrador;
import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public List<Administrador> listarTodos(){ return administradorRepository.findAll(); }

    public Administrador salvar(Administrador administrador){
        System.out.println("to aqui mano :"+administrador);
        try{
            if (administrador == null){
                throw  new RuntimeException("errou");
            }

            administradorRepository.save(administrador);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return administrador;
    }
}

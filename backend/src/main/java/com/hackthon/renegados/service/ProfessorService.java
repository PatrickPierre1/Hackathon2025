package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Administrador;
import com.hackthon.renegados.model.Professor;
import com.hackthon.renegados.model.Usuario;
import com.hackthon.renegados.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public UserDetails save(Professor professor) {
        professor.setPassword(new BCryptPasswordEncoder().encode(professor.getPassword()));
        professor.setRole("PROF");
        return professorRepository.save(professor);
    }

    public Professor editar(Long id, Professor professor) {
        Usuario usuarioExistente = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + id));

        usuarioExistente.setLogin(professor.getLogin());
        usuarioExistente.setRole(professor.getRole());

        // Atualiza a senha apenas se vier uma nova (para não resetar a senha sem querer)
        if (professor.getPassword() != null && !professor.getPassword().isEmpty()) {
            usuarioExistente.setPassword(new BCryptPasswordEncoder().encode(professor.getPassword()));
        }

        return professorRepository.save(professor);
    }

    public Professor buscarPorId(Long id) {
        return professorRepository.findById(id).orElseThrow(() -> new RuntimeException("professor não encontrado com ID: " + id));
    }

    public void deletarPorId(Long id) {
        professorRepository.deleteById(id);
    }


}

package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Aluno salvar(Aluno aluno) {
        if (aluno == null) {
            throw new RuntimeException("Aluno não pode ser nulo.");
        }

        String cpfNumerico = aluno.getCpf() != null ? aluno.getCpf().replaceAll("\\D", "") : "";
        aluno.setPassword(new BCryptPasswordEncoder().encode(cpfNumerico));

        aluno.setLogin(aluno.getRa());
        aluno.setRole("ALUNO");

        return alunoRepository.save(aluno);
    }

    public List<Aluno> salvarTodos(List<Aluno> alunos) {
        for (Aluno aluno : alunos) {
            if (aluno == null) {
                throw new RuntimeException("Aluno nulo encontrado na lista.");
            }

            String cpfNumerico = aluno.getCpf() != null ? aluno.getCpf().replaceAll("\\D", "") : "";
            aluno.setPassword(new BCryptPasswordEncoder().encode(cpfNumerico));

            aluno.setLogin(aluno.getRa());
            aluno.setRole("ALUNO");
        }

        return alunoRepository.saveAll(alunos);
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id).orElse(null);
    }

    public void deletarPorId(Long id) {
        alunoRepository.deleteById(id);
    }
}
